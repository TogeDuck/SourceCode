package com.idle.togeduck.my_record.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryTourResponse(
    @SerialName("event_id") val eventId: Int,
    @SerialName("event_name") val eventName: String,
    val name: String,
    val latitude: String,
    val longitude: String
)
