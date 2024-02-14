package com.idle.togeduck.quest.exchange.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeResponse(
    val id: Long,
    val content: String,
    val image: String,
    val duration: Int,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val expiredAt: LocalDateTime,
    val isMine: Boolean
)

@Serializable
data class ExchangeMyResponse(
    val id: Long,
    val content: String,
    val image: String,
    val duration: Int,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val expiredAt: LocalDateTime,
    val isMine: Boolean
)

@Serializable
data class Exchange(
    val id: Long,
    val content: String,
    val image: String,
    val duration: Int,
    val createdAt: LocalDateTime,
    val expiredAt: LocalDateTime,
    val isMine: Boolean
)

fun ExchangeMyResponse.toExchange() = Exchange(id,content,image,duration,createdAt, expiredAt, isMine)
fun ExchangeResponse.toExchange() = Exchange(id, content, image, duration, createdAt,expiredAt, isMine)