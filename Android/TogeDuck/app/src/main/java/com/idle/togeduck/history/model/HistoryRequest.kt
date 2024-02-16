package com.idle.togeduck.history.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryRequest(
    @SerialName("celebrity-id") val celebrityId: Long
)
