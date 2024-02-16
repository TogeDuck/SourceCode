package com.idle.togeduck.quest.recruit.model

import com.idle.togeduck.event.model.LikeEvent
import com.idle.togeduck.event.model.LikeEventRequest
import kotlinx.serialization.Serializable

@Serializable
data class RecruitRequest(
    val title: String,
    val destinationId: Long,
    val maximum: Int,
    val duration: Int
)