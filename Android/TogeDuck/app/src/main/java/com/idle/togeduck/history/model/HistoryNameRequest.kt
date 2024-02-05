package com.idle.togeduck.history.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryNameRequest(
    @SerialName("history_name") val historyName: String
)
