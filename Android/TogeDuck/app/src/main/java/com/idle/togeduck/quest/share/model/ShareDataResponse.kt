package com.idle.togeduck.quest.share.model

import com.idle.togeduck.common.model.PageableResponse
import com.idle.togeduck.common.model.SortResponse
import kotlinx.serialization.Serializable

@Serializable
data class ShareDataResponse (
    val content: List<ShareResponse>,
    val pageable: PageableResponse,
    val size: Int,
    val number: Int,
    val sort: SortResponse,
    val first: Boolean = false,
    val last: Boolean = false,
    val numberOfElements: Int,
    val empty: Boolean = false
)