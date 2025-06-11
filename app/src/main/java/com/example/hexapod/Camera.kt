package com.example.hexapod

import HandGestureDetector
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState



@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    gestureDetector: HandGestureDetector? = null,
    onError: ((String) -> Unit)? = null
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder()
                        .build()
                        .also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                    // ImageAnalysis use case dla wykrywania gestów
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    // Analizator obrazu
                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                        gestureDetector?.let { detector ->
                            try {
                                val bitmap = imageProxy.convertToBitmap()
                                detector.detectGesture(bitmap)
                            } catch (e: Exception) {
                                Log.e("CameraPreview", "Gesture detection failed: ${e.message}")
                                onError?.invoke("Gesture detection failed: ${e.message}")
                            }
                        }
                        imageProxy.close()
                    }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (exc: Exception) {
                    Log.e("Camera", "Use case binding failed", exc)
                    onError?.invoke("Camera binding failed: ${exc.message}")
                }
            }, ContextCompat.getMainExecutor(context))

            previewView
        })
}

// Poprawiona funkcja konwersji ImageProxy na Bitmap z transformacją dla orientacji pionowej
fun ImageProxy.convertToBitmap(): Bitmap {
    return when (format) {
        ImageFormat.YUV_420_888 -> {
            val yBuffer = planes[0].buffer
            val uBuffer = planes[1].buffer
            val vBuffer = planes[2].buffer

            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()

            val nv21 = ByteArray(ySize + uSize + vSize)

            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
            val outputStream = java.io.ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, outputStream)
            val imageBytes = outputStream.toByteArray()
            
            val originalBitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            
            // Transformacja dla telefonu pionowego:
            // 1. Obrót o 270 stopni (90° + 180°) aby prawidłowo zorientować gesty
            // 2. Odbicie lustrzane dla kamery przedniej
            val matrix = android.graphics.Matrix().apply {
                postRotate(270f) // Obrót o 270 stopni
                postScale(-1f, 1f) // Odbicie lustrzane dla kamery przedniej
            }
            
            android.graphics.Bitmap.createBitmap(
                originalBitmap, 
                0, 
                0, 
                originalBitmap.width, 
                originalBitmap.height, 
                matrix, 
                true
            ).also {
                originalBitmap.recycle() // Zwolnienie pamięci
            }
        }
        else -> {
            // Fallback dla innych formatów
            val buffer = planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            val originalBitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            
            // Ta sama transformacja dla fallback
            val matrix = android.graphics.Matrix().apply {
                postRotate(270f) // Obrót o 270 stopni
                postScale(-1f, 1f) // Odbicie lustrzane dla kamery przedniej
            }
            
            android.graphics.Bitmap.createBitmap(
                originalBitmap, 
                0, 
                0, 
                originalBitmap.width, 
                originalBitmap.height, 
                matrix, 
                true
            ).also {
                originalBitmap.recycle()
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionScreen(
    onPermissionGranted: @Composable () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val permissionStatus = cameraPermissionState.status

    when (permissionStatus) {
        is PermissionStatus.Granted -> {
            onPermissionGranted()
        }
        is PermissionStatus.Denied -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val textToShow = if (permissionStatus.shouldShowRationale) {
                    "App needs permission to use the video camera."
                } else {
                    "Permission is needed for this function."
                }

                Text(textToShow, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { cameraPermissionState.launchPermissionRequest() }
                ) {
                    Text("Grant permission")
                }
            }
        }
    }
}


