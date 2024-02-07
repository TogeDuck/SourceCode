package com.idle.togeduck.myquest.model

import kotlinx.serialization.Serializable

@Serializable
data class MyQuestResponse (
    val chatId : Long,
    val eventId : Long,
    val title : String,
    val type : String
)


data class MyQuest (
    val chatId : Long,
    val eventId : Long,
    val title : String,
    val type : String
)

fun MyQuestResponse.toMyQuest() = MyQuest(chatId, eventId, title, type)