package com.idle.togeduck.history.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryTourResponse(
    @SerialName("event_id") val eventId: Long,
    @SerialName("event_name") val eventName: String,
    val name: String,
    val latitude: String,
    val longitude: String
)

data class HistoryTour(
    val eventId: Long,
    val eventName: String,
    val name: String,
    val latitude: String,
    val longitude: String
)

fun HistoryTourResponse.toHistoryTour() = HistoryTour(eventId, eventName, name, latitude, longitude)