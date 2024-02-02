package com.idle.togeduck.quest.exchange.model

import kotlinx.serialization.Serializable

@Serializable
data class PostExchangeRequest(
    val image: String, // TODO. 이미지 파일로 수정
    val content: String,
    val duration: Int
)
