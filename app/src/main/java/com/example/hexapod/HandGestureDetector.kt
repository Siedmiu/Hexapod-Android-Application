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
    private var lastLandmarks: List<Pair<Float, Float>>? = null

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
        try {
            var detectedGesture: String? = null
            var confidence = 0f
            var currentPosition = lastPosition
            var landmarks: List<Pair<Float, Float>>? = null

            if (result.gestures().isNotEmpty()) {
                val gesture = result.gestures()[0]
                if (gesture.isNotEmpty()) {
                    val recognizedGesture = gesture[0].categoryName()
                    confidence = gesture[0].score()
                    detectedGesture = recognizedGesture

                    // Pozycja dłoni i landmarki
                    if (result.landmarks().isNotEmpty()) {
                        val handLandmarks = result.landmarks()[0]
                        if (handLandmarks.isNotEmpty()) {
                            val wrist = handLandmarks[0] // WRIST landmark
                            
                            // Transformacja koordynatów z powrotem do orientacji pionowej
                            // MediaPipe zwraca koordynaty dla obrazu obróconego o 270°
                            // Musimy je przekształcić z powrotem do orientacji pionowej
                            val transformedX = wrist.y() // y staje się x
                            val transformedY = 1.0f - wrist.x() // x staje się y (odwrócone)
                            
                            currentPosition = Pair(transformedX, transformedY)

                            // Zbierz wszystkie landmarki dla wizualizacji z transformacją
                            landmarks = handLandmarks.map { landmark ->
                                val x = landmark.y() // y staje się x
                                val y = 1.0f - landmark.x() // x staje się y (odwrócone)
                                Pair(x, y)
                            }
                            lastLandmarks = landmarks

                            if (currentPosition != lastPosition) {
                                gestureListener?.onHandPositionChanged(currentPosition)
                            }
                        }
                    }
                }
            }

            // Przekaż landmarki do listenera
            landmarks?.let {
                try {
                    gestureListener?.onLandmarksDetected(it)
                } catch (e: Exception) {
                    println("Error in landmarks callback: ${e.message}")
                }
            }

            handleGestureChange(detectedGesture, currentPosition, confidence)
            lastPosition = currentPosition

        } catch (e: Exception) {
            println("Error in processGestureResult: ${e.message}")
        }
    }

    private fun handleGestureChange(detectedGesture: String?, position: Pair<Float, Float>, confidence: Float) {
        try {
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
        } catch (e: Exception) {
            println("Error in handleGestureChange: ${e.message}")
        }
    }

    fun detectGesture(bitmap: Bitmap) {
        try {
            val mpImage = BitmapImageBuilder(bitmap).build()
            val frameTime = System.currentTimeMillis()

            gestureRecognizer?.recognizeAsync(mpImage, frameTime)
        } catch (e: Exception) {
            println("Error in detectGesture: ${e.message}")
        }
    }

    fun getLastGesture(): String? = lastGesture
    fun getLastPosition(): Pair<Float, Float> = lastPosition
    fun getLastLandmarks(): List<Pair<Float, Float>>? = lastLandmarks

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
    fun onLandmarksDetected(landmarks: List<Pair<Float, Float>>) {}
}
