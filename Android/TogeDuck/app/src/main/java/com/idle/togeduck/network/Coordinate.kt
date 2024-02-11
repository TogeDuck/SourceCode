package com.idle.togeduck.network

import kotlinx.serialization.Serializable


@Serializable
data class WebSocketResponse(
    val chatId: Long,
    val content: String
)

@Serializable
data class WebSocketDataResponse(
    val type: String,
    val celebrityId: Long,
    val data: String
)

@Serializable
data class Coordinate(
    val latitude: Double,
    val longitude: Double,
    val userId: String
)

@Serializable
data class QuestAlert(
    val questType: String,
    val eventId: Long
)

@Serializable
data class Chat(
    val userId: String,
    val chatId: Long,
    val message: String
)