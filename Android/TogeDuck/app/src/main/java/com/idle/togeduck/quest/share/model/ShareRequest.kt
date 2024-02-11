package com.idle.togeduck.quest.share.model

import com.idle.togeduck.quest.exchange.model.ExchangeRequest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class ShareRequest (
    val title: String,
    val content: String,
    val duration: Int
)

data class ShareReq(
    val title: String,
    val content: String,
    val duration: Int
)

fun ShareRequest.toMultipartBody(): MultipartBody.Part {
    val json = Json.encodeToString(ShareRequest.serializer(), this)
    val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("shareRequestDto", json, requestBody)
}

fun ShareReq.toShareRequest() = ShareRequest(title, content, duration)