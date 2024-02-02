package com.idle.togeduck.favorite.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CelebrityResponse (
    val id: Long,
    val name: String,
    val nickname: String,
    val birthday: LocalDate,
    val image: String,
    val teamName: String
)