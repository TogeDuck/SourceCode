package com.idle.togeduck.quest.share.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable
import okhttp3.RequestBody

@Serializable
data class ShareResponse (
    val id: Long,
    val image: String,
    val title: String,
    val content: String,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createdAt: LocalDateTime,
    val isMine: Boolean = false
)

data class Share (
    val id: Long,
    val image: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isMine: Boolean = false
)

fun ShareResponse.shareResponseToShare() = Share(id, image, title, content, createdAt,isMine)

