package com.idle.togeduck.model.dto

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
