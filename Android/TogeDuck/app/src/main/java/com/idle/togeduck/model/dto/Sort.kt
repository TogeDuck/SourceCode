package com.idle.togeduck.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)
