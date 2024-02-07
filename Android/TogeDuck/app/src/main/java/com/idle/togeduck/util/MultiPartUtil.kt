package com.idle.togeduck.util

import android.content.Context
import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat.requireContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

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

    fun uriToFilePath(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri,null, null, null, null)
        lateinit var filePath: String

        cursor?.use { cursor ->
            if(cursor.moveToFirst()){
                val displayNameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val displayName = cursor.getString(displayNameIndex)

                val inputStream = contentResolver.openInputStream(uri)
                val targetFile = File(context.cacheDir, displayName)
                inputStream?.use { input ->
                    FileOutputStream(targetFile).use { output ->
                        input.copyTo(output)
                    }
                }
                filePath = targetFile.absolutePath
            }
        }
        return filePath
    }


}