package com.idle.togeduck.event.model

import kotlinx.serialization.Serializable

@Serializable
data class EventLikesDataResponse(
    val past: List<EventResponse>,
    val today: List<EventResponse>,
    val later: List<EventResponse>
)
