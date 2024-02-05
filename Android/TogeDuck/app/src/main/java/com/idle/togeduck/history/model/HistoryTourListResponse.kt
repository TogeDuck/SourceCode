package com.idle.togeduck.history.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryTourListResponse(
    val code: Int,
    val message: String,
    val data: HistoryTourDataResponse
)
