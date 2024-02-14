package com.idle.togeduck.history.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryListResponse(
    val code: Long,
    val message: String,
    val data: List<HistoryDataResponse>
)
