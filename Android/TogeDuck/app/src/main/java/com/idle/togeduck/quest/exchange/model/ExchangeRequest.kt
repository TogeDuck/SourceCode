package com.idle.togeduck.quest.exchange.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRequest(
    val content: String,
    val duration: Int
)

data class ExchangeReq(
    val content: String,
    val duration: Int
)

fun ExchangeReq.toExchangeRequest() = ExchangeRequest(content, duration)