package com.idle.togeduck.quest.share.model

import kotlinx.serialization.Serializable

@Serializable
data class ShareRequest (
//    val image: String,
    val title: String,
    val content: String,
    val duration: Int
)
