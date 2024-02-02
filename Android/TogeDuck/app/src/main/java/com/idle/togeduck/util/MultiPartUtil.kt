package com.idle.togeduck.util

import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MultiPartUtil {
    fun createImagePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    fun createImagePart(uri: String): MultipartBody.Part {
        val file = File(uri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    fun createRequestBody(content: Int): RequestBody {
        return content.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun createRequestBody(content: String): RequestBody {
        return content.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}