package com.idle.togeduck.favorite.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequest (
    val celcbrityId: Long
)