package com.idle.togeduck.event.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventReviewContentResponse(
    @SerialName("review_id") val reviewId: Int,
    val content: String,
    val isMine: Boolean,
    @SerialName("review_image_url") val reviewImageUrl: String
)

data class EventReviewContent(
    val reviewId: Int,
    val content: String,
    val isMine: Boolean,
    val reviewImageUrl: String
)

fun EventReviewContentResponse.toEventReviewContent() = EventReviewContent(
    reviewId, content, isMine, reviewImageUrl
)