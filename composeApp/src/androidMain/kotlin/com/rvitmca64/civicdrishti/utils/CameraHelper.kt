package com.rvitmca64.civicdrishti.utils

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executor

class CameraHelper(private val context: Context) {

    private var imageCapture: ImageCapture? = null

    /**
     * Start camera preview
     */
    suspend fun startCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ): ImageCapture {
        return withContext(Dispatchers.Main) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // Image capture
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            // Camera selector
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind all use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                throw e
            }

            imageCapture!!
        }
    }

    /**
     * Capture photo and return byte array
     */
    suspend fun capturePhoto(
        imageCapture: ImageCapture,
        outputDirectory: File
    ): Result<Pair<ByteArray, String>> {
        return withContext(Dispatchers.IO) {
            try {
                // Create output file
                val photoFile = File(
                    outputDirectory,
                    "report_${System.currentTimeMillis()}.jpg"
                )

                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                // Capture image
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            // Success handled by coroutine
                        }

                        override fun onError(exception: ImageCaptureException) {
                            // Error handled by coroutine
                        }
                    }
                )

                // Wait for file to be written
                var attempts = 0
                while (!photoFile.exists() && attempts < 50) {
                    kotlinx.coroutines.delay(100)
                    attempts++
                }

                if (!photoFile.exists()) {
                    return@withContext Result.failure(Exception("Failed to capture image"))
                }

                // Read bytes
                val bytes = photoFile.readBytes()
                Result.success(Pair(bytes, photoFile.absolutePath))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Get output directory for photos
     */
    fun getOutputDirectory(): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, "ReportImages").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }
}