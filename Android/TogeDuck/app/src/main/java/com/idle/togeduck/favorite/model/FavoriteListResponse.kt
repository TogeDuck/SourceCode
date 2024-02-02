package com.idle.togeduck.favorite.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteListResponse (
    val code: Int,
    val message: String,
    val data: List<CelebrityResponse>
)