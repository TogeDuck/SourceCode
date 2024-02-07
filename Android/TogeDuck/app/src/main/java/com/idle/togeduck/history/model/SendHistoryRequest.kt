package com.idle.togeduck.history.model

import kotlinx.serialization.Serializable

@Serializable
data class SendHistoryRequest(
    val latitude: Double,
    val longitude: Double
)
