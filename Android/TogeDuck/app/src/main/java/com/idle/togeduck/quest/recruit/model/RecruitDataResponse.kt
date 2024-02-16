package com.idle.togeduck.quest.recruit.model

import com.idle.togeduck.common.model.PageableResponse
import com.idle.togeduck.common.model.SortResponse
import kotlinx.serialization.Serializable

@Serializable
data class RecruitDataResponse(
    val content: List<RecruitResponse>,
    val pageable: PageableResponse,
    val size: Int,
    val number: Int,
    val sort: SortResponse,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
