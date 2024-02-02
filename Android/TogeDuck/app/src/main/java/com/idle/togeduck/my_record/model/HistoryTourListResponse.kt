package com.idle.togeduck.my_record.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryTourListResponse(
    val status: Int,
    val message: String,
    val data: List<HistoryTourDataResponse>
)
