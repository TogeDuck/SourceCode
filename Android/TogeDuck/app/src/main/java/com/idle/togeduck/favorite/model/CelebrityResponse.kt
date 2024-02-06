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

data class Celebrity (
    val id: Long,
    val name: String,
    val nickname: String,
    val birthday: LocalDate,
    val image: String,
    val teamName: String,
    var isSelected: Boolean = false,
    var isClicked: Boolean = false
)


fun CelebrityResponse.celebrityResponseToCelebrity() = Celebrity(id, name, nickname, birthday, image, teamName)
fun Celebrity.celebrityToFavoriteRequest() = FavoriteRequest(id)