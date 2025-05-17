package com.cltb.initiative.conversign.analyzer

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class ImageAnalyzer(context: Context, private val onAnswerCaptured: (String) -> Unit) : ImageAnalysis.Analyzer {

    private val aiLoader = AILoader(context)
    private val labels = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z"
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val image = imageProxy.image
        if (image != null) {
            // Convert image to Bitmap or ByteBuffer
            val bitmap = imageProxy.toBitmap() // You'll implement this

            // Preprocess bitmap to fit model input (e.g. 224x224)
            val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

            // Run model inference
            val result = aiLoader.runModelInference(resized)
            val label = getTopLabel(result)
            println("Label: $label")
            // Update UI with result if needed
            onAnswerCaptured.invoke(label)


            imageProxy.close()
        }
    }

    private fun getTopLabel(result: FloatArray): String {
        val maxIndex = result.indices.maxByOrNull { result[it] } ?: return "Unknown"
        return labels.getOrNull(maxIndex) ?: "Unknown"
    }
}
