package com.idle.togeduck.quest.exchange.model

import com.idle.togeduck.common.model.PageableResponse
import com.idle.togeduck.common.model.SortResponse
import com.idle.togeduck.quest.recruit.model.RecruitResponse
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeDataResponse(
    val content: List<ExchangeResponse>,
    val pageable: PageableResponse,
    val size: Int,
    val number: Int,
    val sort: SortResponse,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
