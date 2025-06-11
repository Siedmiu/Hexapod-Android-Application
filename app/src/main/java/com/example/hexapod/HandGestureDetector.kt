import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult
import com.google.mediapipe.tasks.vision.core.RunningMode
import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.tasks.core.BaseOptions
import kotlin.math.sqrt

class HandGestureDetector(private val context: Context) {
    private var gestureRecognizer: GestureRecognizer? = null
    private var lastGesture: String? = null
    private var lastPosition: Pair<Float, Float> = Pair(0f, 0f)
    private var gestureListener: GestureListener? = null
    private var gestureStartTime: Long = 0
    private val gestureConfirmationThreshold = 500L

    init {
        setupGestureRecognizer()
    }

    fun setGestureListener(listener: GestureListener?) {
        gestureListener = listener
    }

    private fun setupGestureRecognizer() {
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath("gesture_recognizer.task")
            .build()

        val options = GestureRecognizer.GestureRecognizerOptions.builder()
            .setBaseOptions(baseOptions)
            .setMinHandDetectionConfidence(0.5f)
            .setMinTrackingConfidence(0.5f)
            .setMinHandPresenceConfidence(0.5f)
            .setRunningMode(RunningMode.LIVE_STREAM)
            .setResultListener(this::processGestureResult)
            .setErrorListener { error ->
                println("Gesture recognition error: ${error.message}")
            }
            .build()

        gestureRecognizer = GestureRecognizer.createFromOptions(context, options)
    }

    private fun processGestureResult(result: GestureRecognizerResult, image: MPImage) {
        var detectedGesture: String? = null
        var confidence = 0f
        var currentPosition = lastPosition

        if (result.gestures().isNotEmpty()) {
            val gesture = result.gestures()[0]
            if (gesture.isNotEmpty()) {
                val recognizedGesture = gesture[0].categoryName()
                confidence = gesture[0].score()

                // Mapowanie gestów MediaPipe na własne gesty
                detectedGesture = mapToCustomGesture(recognizedGesture)

                // Pozycja dłoni z landmarks
                if (result.landmarks().isNotEmpty()) {
                    val handLandmarks = result.landmarks()[0]
                    if (handLandmarks.isNotEmpty()) {
                        val wrist = handLandmarks[0] // WRIST landmark
                        val x = wrist.x()
                        val y = wrist.y()
                        currentPosition = Pair(x, y)

                        if (currentPosition != lastPosition) {
                            gestureListener?.onHandPositionChanged(currentPosition)
                        }
                    }
                }
            }
        }

        handleGestureChange(detectedGesture, currentPosition, confidence)

        lastPosition = currentPosition
    }

    private fun handleGestureChange(detectedGesture: String?, position: Pair<Float, Float>, confidence: Float) {
        val currentTime = System.currentTimeMillis()

        when {
            // Nowy gest wykryty
            detectedGesture != null && detectedGesture != lastGesture -> {
                lastGesture = detectedGesture
                gestureStartTime = currentTime

                // Natychmiastowe powiadomienie o nowym geście
                gestureListener?.onGestureDetected(detectedGesture, position, confidence)
            }

            // Ten sam gest kontynuowany
            detectedGesture != null && detectedGesture == lastGesture -> {
                // Gest potwierdzony po określonym czasie
                if (currentTime - gestureStartTime > gestureConfirmationThreshold) {
                    gestureListener?.onGestureDetected(detectedGesture, position, confidence)
                    gestureStartTime = currentTime // Reset czasu dla kolejnych powiadomień
                }
            }

            // Brak gestu - gest utracony
            detectedGesture == null && lastGesture != null -> {
                lastGesture = null
                gestureListener?.onGestureLost()
            }
        }
    }

    private fun mapToCustomGesture(mediaPipeGesture: String): String? {
        return when (mediaPipeGesture) {
            "Thumb_Up" -> "thumbs_up"
            "Pointing_Up" -> "index_up"
            "Open_Palm" -> "okay"
            "Victory" -> "peace"
            "ILoveYou" -> "rock"
            else -> null
        }
    }

    fun detectGesture(bitmap: Bitmap) {
        val mpImage = BitmapImageBuilder(bitmap).build()
        val frameTime = System.currentTimeMillis()

        gestureRecognizer?.recognizeAsync(mpImage, frameTime)
    }

    fun getLastGesture(): String? = lastGesture
    fun getLastPosition(): Pair<Float, Float> = lastPosition

    fun close() {
        gestureRecognizer?.close()
        gestureRecognizer = null
        gestureListener = null
    }
}
interface GestureListener {
    fun onGestureDetected(gesture: String, position: Pair<Float, Float>, confidence: Float)
    fun onGestureLost()
    fun onHandPositionChanged(position: Pair<Float, Float>)
}
