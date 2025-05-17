package com.cltb.initiative.conversign.utils

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import com.cltb.initiative.conversign.data.Educator
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.data.Student
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.media.Image
import android.os.Build
import android.util.Log
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import androidx.camera.core.ImageProxy


fun DocumentSnapshot.toEducator(): Educator {
    val data = data?.toMutableMap() ?: mutableMapOf()
    data["id"] = id
    val json = Gson().toJson(data)
    return Gson().fromJson(json, Educator::class.java)
}

fun DocumentSnapshot.toStudent(): Student {
    val data = data?.toMutableMap() ?: mutableMapOf()
    data["id"] = id
    val json = Gson().toJson(data)
    return Gson().fromJson(json, Student::class.java)
}

fun DocumentSnapshot.toProgress(): Progress {
    val data = data?.toMutableMap() ?: mutableMapOf()
    data["id"] = id
    val json = Gson().toJson(data)
    return Gson().fromJson(json, Progress::class.java)
}

@OptIn(ExperimentalGetImage::class)
fun ImageProxy.toBitmap(): Bitmap? {
    val yuvImage = image?.toYuvImage(this.imageInfo.rotationDegrees) ?: return null
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
    val imageBytes = out.toByteArray()
    return android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

private fun Image.toYuvImage(rotationDegrees: Int): YuvImage? {
    if (format != ImageFormat.YUV_420_888) return null

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

    return YuvImage(nv21, ImageFormat.NV21, width, height, null)
}
