package com.idle.togeduck.history.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryTourResponse(
    @SerialName("event_id") val eventId: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

data class HistoryTour(
    val eventId: Long,
    val eventName: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

fun HistoryTourResponse.toHistoryTour() = HistoryTour(eventId, name, "나의 투어",latitude, longitude)