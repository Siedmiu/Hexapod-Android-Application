import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult

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

                    // Hands positions
                    if (result.landmarks().isNotEmpty()) {
                        val handLandmarks = result.landmarks()[0]
                        if (handLandmarks.isNotEmpty()) {
                            val wrist = handLandmarks[0] // WRIST landmark

                            // coordiantes transformation
                            // as MediaPipe coordinates are rotated we need to
                            // re-rotate them back
                            val transformedX = wrist.y()
                            val transformedY = 1.0f - wrist.x()

                            currentPosition = Pair(transformedX, transformedY)

                            // get all landmarks
                            landmarks = handLandmarks.map { landmark ->
                                val x = landmark.y()
                                val y = 1.0f - landmark.x()
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

            // landmarks to listener
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

    private fun handleGestureChange(
        detectedGesture: String?,
        position: Pair<Float, Float>,
        confidence: Float
    ) {
        try {
            val currentTime = System.currentTimeMillis()

            when {
                // gesture detected
                detectedGesture != null && detectedGesture != lastGesture -> {
                    lastGesture = detectedGesture
                    gestureStartTime = currentTime

                    gestureListener?.onGestureDetected(detectedGesture, position, confidence)
                }

                // still the same
                detectedGesture != null && detectedGesture == lastGesture -> {
                    if (currentTime - gestureStartTime > gestureConfirmationThreshold) {
                        gestureListener?.onGestureDetected(detectedGesture, position, confidence)
                        gestureStartTime = currentTime
                    }
                }

                // No gesture - gesture lost
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
        //main fuction for getting recognized gestures
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
