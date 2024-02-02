package com.idle.togeduck.quest.share.model

import kotlinx.serialization.Serializable

@Serializable
data class Share (
    val id: Long,
    val image: String,
    val title: String,
    val content: String,
    val duration: Int
)