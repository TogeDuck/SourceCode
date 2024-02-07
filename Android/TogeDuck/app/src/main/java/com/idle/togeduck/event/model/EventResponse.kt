package com.idle.togeduck.event.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    @SerialName("eventId") val eventId: Long,
    @SerialName("image1") val image1: String? = null,
    @SerialName("image2") val image2: String? = null,
    @SerialName("image3") val image3: String? = null,
    val name: String,
    @Serializable(with = LocalDateIso8601Serializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateIso8601Serializer::class)
    val endDate: LocalDate,
    val latitude: Double,
    val longitude: Double,
    var isStar: Boolean,
    var isVisited: Boolean
)

data class Event(
    val eventId: Long,
    val image1: String?,
    val image2: String?,
    val image3: String?,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val latitude: Double,
    val longitude: Double,
    var isStar: Boolean,
    var isVisited: Boolean
)

fun EventResponse.toEvent(): Event = Event(
    eventId,
    image1,
    image2,
    image3,
    name,
    startDate,
    endDate,
    latitude,
    longitude,
    isStar,
    isVisited
)