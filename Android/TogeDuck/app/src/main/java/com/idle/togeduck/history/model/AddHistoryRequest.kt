package com.idle.togeduck.history.model

import kotlinx.serialization.Serializable

@Serializable
data class AddHistoryRequest(
    val eventId: Long,
    val historyId: Long
)
