package com.idle.togeduck.login.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginDataResponse(
    val grantType: String,
    val userId: Long,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpireDate: Long
)
