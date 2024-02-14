package com.idle.togeduck.history.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateHistoryResponse(
    val code: Long,
    val message: String,
    val data: Long
)
