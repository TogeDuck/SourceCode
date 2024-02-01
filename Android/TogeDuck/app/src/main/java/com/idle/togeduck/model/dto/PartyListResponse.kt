package com.idle.togeduck.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class PartyListResponse(
    val code: Int,
    val message: String,
    val data: List<PartyData>
)
