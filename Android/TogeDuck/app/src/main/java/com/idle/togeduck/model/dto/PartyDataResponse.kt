package com.idle.togeduck.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class PartyDataResponse(
    val content: List<PartyResponse>,
    val pageable: PageableResponse,
    val size: Int,
    val number: Int,
    val sort: SortResponse,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
