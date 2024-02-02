package com.idle.togeduck.event.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikeEventRequest(
    @SerialName("event_id") val eventId: Int
)


data class LikeEvent(
    val eventId: Int
)

fun LikeEvent.toLikeEventRequest() = LikeEventRequest(eventId)