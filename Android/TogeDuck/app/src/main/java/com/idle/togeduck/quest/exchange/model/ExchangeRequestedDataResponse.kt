package com.idle.togeduck.quest.exchange.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRequestedDataResponse(
    val myTrade: ExchangeResponse,
    val trade: ExchangeResponse
)
