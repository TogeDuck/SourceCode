package com.idle.togeduck.common.model

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse (
    val code: Long,
    val message: String
)
