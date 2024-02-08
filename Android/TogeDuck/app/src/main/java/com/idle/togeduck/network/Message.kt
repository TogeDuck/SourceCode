package com.idle.togeduck.network

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val chatId: Long,
    val content: String,
    val userId: Long,
)

@Serializable
data class MessageRequest(
    val chatId: Long,
    val content: String
)

@Serializable
data class MessageResponse(
    val chatId: Long,
    val userId: Long,
    val content: String
)

fun MessageResponse.toMessage() = Message(chatId, content, userId)
fun Message.toMessageRequest() = MessageRequest(chatId, content)