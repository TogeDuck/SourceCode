package com.idle.togeduck.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class PartyData(
    val content: List<Party>,
    val pageable: Pageable,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
