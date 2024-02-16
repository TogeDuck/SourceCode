package com.idle.togeduck.quest.share.model

import kotlinx.serialization.Serializable

@Serializable
data class ShareListResponse (
    val code: Int,
    val message: String,
    val data: ShareDataResponse
)