package com.idle.togeduck.event.model

import com.idle.togeduck.common.model.PageableResponse
import com.idle.togeduck.common.model.SortResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventReviewDataResponse(
    @SerialName("review_id") val reviewId: Int,
    val content: String,
    val isMine: Boolean,
    @SerialName("review_image_url") val reviewImageUrl: String,
    val pageable: PageableResponse,
    val size: Int,
    val number: Int,
    val sort: SortResponse,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
