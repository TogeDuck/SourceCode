package com.idle.togeduck.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class PartyList(
    val code: Int,
    val message: String,
    val data: List<PartyData>
)
