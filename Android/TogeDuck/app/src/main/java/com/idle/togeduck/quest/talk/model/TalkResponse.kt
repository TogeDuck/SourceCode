package com.idle.togeduck.quest.talk.model

import kotlinx.serialization.Serializable

@Serializable
data class TalkResponse(
    val chatId: Long,
    val userId: String,
    val content: String
)

data class Talk(
    val chatId: Long,
    val userId: String,
    val content: String,
    val isMine: Boolean
)

data class TalkRoom(
    val chatId: Long,
    val title: String,
    val users: Map<String, String>
)

fun TalkResponse.toTalk() = Talk(chatId, userId, content, false)