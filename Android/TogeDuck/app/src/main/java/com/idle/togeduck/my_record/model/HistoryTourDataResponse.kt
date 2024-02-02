package com.idle.togeduck.my_record.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryTourDataResponse(
    val route: String,
    @SerialName("history_event") val historyEvent: List<HistoryTourResponse>
)
