package com.idle.togeduck.event.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    @SerialName("event_id") val eventId: Long,
    @SerialName("url") val imgUrl: String,
    val name: String,
    val description: String,
    @Serializable(with = LocalDateIso8601Serializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateIso8601Serializer::class)
    val endDate: LocalDate,
    val latitude: Double,
    val longitude: Double,
    var isStar: Boolean = false,
    var isVisited: Boolean = false
)
