package com.idle.togeduck.event.model

import kotlinx.serialization.Serializable

@Serializable
data class EventListResponse(
    val status: Int,
    val message: String,
    val today: List<EventResponse>,
    val later: List<EventResponse>
)
