package com.idle.togeduck.quest.talk.model

import com.idle.togeduck.common.model.PageableResponse
import com.idle.togeduck.common.model.SortResponse
import kotlinx.serialization.Serializable

@Serializable
data class TalkDataResponse(
    val content: List<TalkResponse>,
    val pageable: PageableResponse,
    val size: Int,
    val number: Int,
    val sort: SortResponse,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
