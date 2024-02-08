package com.idle.togeduck.network

import kotlinx.serialization.Serializable
@Serializable
data class Coordinate(
    val celebrityId: Long,
    val userId: Long,
    val lat: Double,
    val lng: Double
)
@Serializable
data class CoordinateResponse(
    val celebrityId: Long,
    val latitude: Double,
    val longitude: Double,
    val userId: Long
)
@Serializable
data class CoordinateRequest(
    val celebrityId: Long,
    val latitude: Double,
    val longitude: Double,
)

fun CoordinateResponse.toCoordinate() = Coordinate(celebrityId, userId, latitude, longitude)
fun Coordinate.toCoordinateRequest() = CoordinateRequest(celebrityId, lat, lng)