package com.idle.togeduck.event.model

import kotlinx.serialization.Serializable

@Serializable
data class SingleEventResponse(
    val code: Long,
    val message: String,
    val data: EventResponse
)
