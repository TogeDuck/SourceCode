package com.idle.togeduck.quest.talk.model

import kotlinx.serialization.Serializable

@Serializable
data class TalkListResponse(
    val code: Int,
    val message: String,
    val data: TalkDataResponse
)
