package com.idle.togeduck.login.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val code: Long,
    val message: String,
    val data: LoginDataResponse
)
