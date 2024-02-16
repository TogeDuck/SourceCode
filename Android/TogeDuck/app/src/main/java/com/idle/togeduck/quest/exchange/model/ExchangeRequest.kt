package com.idle.togeduck.quest.exchange.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class ExchangeRequest(
    val content: String,
    val duration: Int
)

data class ExchangeReq(
    val content: String,
    val duration: Int
)

fun ExchangeRequest.toMultipartBody(): MultipartBody.Part {
    //객체를 JSON 문자열로 변환
    val json = Json.encodeToString(ExchangeRequest.serializer(), this)
    // JSON 문자열을 RequestBody로 변환
    val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
    // RequestBody를 MultipartBody.Part로 변환하여 반환
    return MultipartBody.Part.createFormData("tradeRequestDto", json, requestBody)
}

fun ExchangeReq.toExchangeRequest() = ExchangeRequest(content, duration)