package com.idle.togeduck.network

import kotlinx.serialization.Serializable
@Serializable
data class Coordinate(
    val celebrityId: Long,
    val latitude: Double,
    val longitude: Double,
    val userId: String
)
@Serializable
data class CoordinateResponse(
    val celebrityId: Long,
    val latitude: Double,
    val longitude: Double,
    val userId: String,
    val type: String
)
@Serializable
data class CoordinateRequest(
    val celebrityId: Long,
    val latitude: Double,
    val longitude: Double,
    val userId: String,
    val type: String
)

@Serializable
data class TempCoordinateResponse(
    val chatId: Long,
    val content: String
)

fun CoordinateResponse.toCoordinate() = Coordinate(celebrityId, latitude, longitude, userId)