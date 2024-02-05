package com.idle.togeduck.quest.recruit.model

import kotlinx.serialization.Serializable

@Serializable
data class RecruitListResponse(
    val code: Int,
    val message: String,
    val data: RecruitDataResponse
)
