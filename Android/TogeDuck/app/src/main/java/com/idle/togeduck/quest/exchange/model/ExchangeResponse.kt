package com.idle.togeduck.quest.exchange.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeResponse(
    val id: Int,
    val content: String,
    val image: String,
    val duration: Int,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createdAt: LocalDateTime,
    val isMine: Boolean
)
