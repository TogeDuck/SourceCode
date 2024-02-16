package com.idle.togeduck.history.model

import kotlinx.serialization.Serializable

@Serializable
data class SendHistoryRequest(
    val route: List<Position>
)
