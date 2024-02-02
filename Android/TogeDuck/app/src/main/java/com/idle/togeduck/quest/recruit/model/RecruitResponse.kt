package com.idle.togeduck.quest.recruit.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecruitResponse(
    @SerialName("id") val chatId: Long,
    val title: String,
    val destination: String,
    val maximum: Int,
    var duration: Int,
    var current: Int,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val expiredAt: LocalDateTime,
    var isMine: Boolean
)

data class Recruit(
     val chatId: Long,
    val title: String,
    val destination: String,
    val maximum: Int,
    var duration: Int,
    var current: Int,
    val createdAt: LocalDateTime,
    val expiredAt: LocalDateTime,
    var isMine: Boolean
)
fun RecruitResponse.recruitResponseToRecruit() = Recruit(chatId, title, destination, maximum, duration, current, createdAt, expiredAt, isMine)
