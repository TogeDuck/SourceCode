package com.idle.togeduck.common.model

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse (
    val code: Int,
    val message: String
)
