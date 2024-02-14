package com.idle.togeduck.event.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventReviewContentResponse(
    @SerialName("id") val reviewId: Long,
    val content: String,
    val isMine: Boolean,
    @SerialName("url") val reviewImageUrl: String?
)

data class EventReviewContent(
    val reviewId: Long,
    val content: String,
    val isMine: Boolean,
    val reviewImageUrl: String?
)

fun EventReviewContentResponse.toEventReviewContent() = EventReviewContent(
    reviewId, content, isMine, reviewImageUrl
)