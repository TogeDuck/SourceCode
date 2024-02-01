package com.idle.togeduck.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class SortResponse(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)
