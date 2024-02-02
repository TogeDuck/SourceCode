package com.idle.togeduck.model.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Party(
    @SerialName("id") val chatId: Long,
    val title: String,
    val destination: String,
    val maximum: Int,
    var duration: Int,
    var current: Int,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val expiredAt: LocalDateTime
)

