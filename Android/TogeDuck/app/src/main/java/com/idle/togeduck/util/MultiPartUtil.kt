package com.idle.togeduck.util

import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object MultiPartUtil {
    fun createImagePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    fun createImagePart(uri: String): MultipartBody.Part {
        val file = File(uri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    fun createRequestBody(content: Int): MultipartBody.Part {
        val requestBody = content.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("content", "content", requestBody)
    }

    fun createRequestBody(content: String): MultipartBody.Part {
        val requestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("content", "content", requestBody)
    }
}