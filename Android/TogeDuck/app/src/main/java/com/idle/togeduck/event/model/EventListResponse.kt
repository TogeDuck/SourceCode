package com.idle.togeduck.event.model

import kotlinx.serialization.Serializable

@Serializable
data class EventListResponse(
    val code: Int,
    val message: String,
    val data: EventDataResponse
)
