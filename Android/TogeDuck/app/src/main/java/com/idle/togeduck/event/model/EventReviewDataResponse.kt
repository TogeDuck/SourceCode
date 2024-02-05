package com.idle.togeduck.event.model

import com.idle.togeduck.common.model.PageableResponse
import com.idle.togeduck.common.model.SortResponse
import kotlinx.serialization.Serializable

@Serializable
data class EventReviewDataResponse(
    val content: List<EventReviewContentResponse>,
    val pageable: PageableResponse,
    val size: Int,
    val number: Int,
    val sort: SortResponse,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)

data class EventReviewData(
    val content: List<EventReviewContentResponse>,
    val pageable: PageableResponse,
    val size: Int,
    val number: Int,
    val sort: SortResponse,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)

fun EventReviewDataResponse.toEventReviewData() = EventReviewData(
    content, pageable, size, number, sort, first, last, numberOfElements, empty
)
