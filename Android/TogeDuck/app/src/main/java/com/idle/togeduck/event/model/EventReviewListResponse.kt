package com.idle.togeduck.event.model

import kotlinx.serialization.Serializable

@Serializable
data class EventReviewListResponse(
    val code: Int,
    val message: String,
    val data: List<EventReviewDataResponse>
)