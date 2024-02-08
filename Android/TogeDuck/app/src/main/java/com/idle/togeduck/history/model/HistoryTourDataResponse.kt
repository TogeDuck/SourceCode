package com.idle.togeduck.history.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryTourDataResponse(
    val route: List<Position>,
    @SerialName("history_event") val historyEvent: List<HistoryTourResponse>
)
