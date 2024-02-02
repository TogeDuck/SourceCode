package com.idle.togeduck.quest.talk.model

import kotlinx.serialization.Serializable

@Serializable
data class TalkResponse(
    val chatId: Int,
    val userId: Int,
    val content: String
)

data class Talk(
    val chatId: Int,
    val userId: Int,
    val content: String
)

fun TalkResponse.toTalk() = Talk(chatId, userId, content)