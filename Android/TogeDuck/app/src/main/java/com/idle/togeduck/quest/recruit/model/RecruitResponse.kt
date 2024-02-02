package com.idle.togeduck.quest.recruit.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class RecruitResponse(
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

