package com.idle.togeduck.util

import android.content.Context
import android.graphics.Bitmap
import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

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

    //todo. 임시 추가 (확인 ..)
    fun createRequestBody2(content: String): RequestBody {
        val requestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        return requestBody
    }
    fun createRequestBody2(content: Int): RequestBody {
        val requestBody = content.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        return requestBody
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

    //todo. 용량 줄인 버전
    fun uriToFilePath2(context: Context, bitmap: Bitmap, uri: Uri): String {
        val maxImgSize = 50 * 10000 //저용량 변환 중 최대 사이즈
        var realImgSize = maxImgSize
        var quality = 100 //사진 퀄리티 100부터 줄여나가며 용량 맞춤

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

                while(realImgSize >= maxImgSize) {
                    if(quality < 0){
                        return "tobig"
                    }
                    val out = FileOutputStream(targetFile)

                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
                    realImgSize = targetFile.length().toInt() //작아진 파일의 크기 저장하며 비교

                    quality -= 20

                    out.close()
                }
                Log.d("uriToPath로그", "이미지 사이즈 : $realImgSize")

                filePath = targetFile.absolutePath
            }
        }
        return filePath
    }


}