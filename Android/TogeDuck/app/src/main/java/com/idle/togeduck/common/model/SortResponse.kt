package com.idle.togeduck.common.model

import kotlinx.serialization.Serializable

@Serializable
data class SortResponse(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)
