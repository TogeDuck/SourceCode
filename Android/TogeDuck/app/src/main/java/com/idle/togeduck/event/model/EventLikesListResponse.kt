package com.idle.togeduck.event.model

import kotlinx.serialization.Serializable

@Serializable
data class EventLikesListResponse(
    val code: Int,
    val message: String,
    val data: EventLikesDataResponse
)
