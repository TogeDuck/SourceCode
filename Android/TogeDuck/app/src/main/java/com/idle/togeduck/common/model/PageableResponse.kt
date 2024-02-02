package com.idle.togeduck.common.model

import kotlinx.serialization.Serializable

@Serializable
data class PageableResponse(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: SortResponse,
    val offset: Int,
    val unpaged: Boolean,
    val paged: Boolean
)
