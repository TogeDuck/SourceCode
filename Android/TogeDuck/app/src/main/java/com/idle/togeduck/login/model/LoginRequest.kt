package com.idle.togeduck.login.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val socialType: String,
    val code: String
)
