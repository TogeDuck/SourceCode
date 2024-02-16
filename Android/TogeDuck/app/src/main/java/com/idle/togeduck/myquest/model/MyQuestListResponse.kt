package com.idle.togeduck.myquest.model

import kotlinx.serialization.Serializable

@Serializable
data class MyQuestListResponse (
    val code: Int,
    val message: String,
    val data: List<MyQuestResponse>
)