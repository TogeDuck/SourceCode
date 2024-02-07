package com.idle.togeduck.network

import kotlinx.serialization.Serializable

data class Quest (
    val questId: Long,
    val questKind: String
)

data class Coordinate(
    val eventId: Long,
    val userId: Long,
    val lat: Double,
    val lng: Double
)

@Serializable
data class Message(
    val chatId: Long,
    val content: String
)