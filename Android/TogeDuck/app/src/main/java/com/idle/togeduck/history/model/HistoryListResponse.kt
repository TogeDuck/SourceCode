package com.idle.togeduck.history.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryListResponse(
    val code: Int,
    val message: String,
    val data: List<HistoryDataResponse>
)
