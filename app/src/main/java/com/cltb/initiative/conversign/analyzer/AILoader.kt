    package com.cltb.initiative.conversign.analyzer

    import android.content.Context
    import android.graphics.Bitmap
    import android.util.Log
    import org.tensorflow.lite.Interpreter
    import java.io.FileInputStream
    import java.nio.ByteBuffer
    import java.nio.ByteOrder
    import java.nio.MappedByteBuffer
    import java.nio.channels.FileChannel

    class AILoader(private val context: Context) {

        companion object {
            private const val NUM_CLASSES = 24  // ← updated from 4 to 24
        }
        private val tflite = Interpreter(loadModelFile("model.tflite"))

        fun runModelInference(bitmap: Bitmap): FloatArray {
            Log.d("AILoader", "Input shape: ${tflite.getInputTensor(0).shape().contentToString()}")
            val inputBuffer = preprocessBitmapToByteBuffer(bitmap)
            val output = Array(1) { FloatArray(NUM_CLASSES) }
            tflite.run(inputBuffer, output)
            return output[0]
        }

        private fun preprocessBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
            val inputSize = 64
            val byteBuffer = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 1 * 4) // Grayscale = 1 channel
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(inputSize * inputSize)
            bitmap.getPixels(intValues, 0, inputSize, 0, 0, inputSize, inputSize)

            for (pixelValue in intValues) {
                val r = (pixelValue shr 16 and 0xFF)
                val g = (pixelValue shr 8 and 0xFF)
                val b = (pixelValue and 0xFF)

                val gray = (0.2989f * r + 0.5870f * g + 0.1140f * b) / 255.0f
                byteBuffer.putFloat(gray)
            }

            return byteBuffer
        }

        private fun loadModelFile(modelName: String): MappedByteBuffer {
            val fileDescriptor = context.assets.openFd(modelName)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }

    }