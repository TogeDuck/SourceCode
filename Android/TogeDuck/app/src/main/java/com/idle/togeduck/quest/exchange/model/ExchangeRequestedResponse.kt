package com.idle.togeduck.quest.exchange.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRequestedResponse(
    val code: Long,
    val message: String,
    val data: ExchangeRequestedDataResponse
)
