package com.idle.togeduck.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventList(
    val status: Int,
    val message: String,
    val today: List<Event>,
    val later: List<Event>
)
