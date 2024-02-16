package com.idle.togeduck.quest.exchange.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeListResponse(
    val code: Int,
    val message: String,
    val data: ExchangeDataResponse
)

@Serializable
data class ExchangeMyListResponse(
    val code: Int,
    val message: String,
    val data: ExchangeDataMyResponse
)