package com.idle.togeduck.quest.share.model

import kotlinx.serialization.Serializable
import okhttp3.RequestBody

@Serializable
data class ShareResponse (
    val id: Long,
    val image: String,
    val title: String,
    val content: String,
    val duration: Int,
    val isMine: Boolean = false
)

data class Share (
    val id: Long,
    val image: String,
    val title: String,
    val content: String,
    val duration: Int,
    val isMine: Boolean = false
)

fun ShareResponse.shareResponseToShare() = Share(id, image, title, content, duration, isMine)

