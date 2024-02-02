package com.idle.togeduck.my_record.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryDataResponse(
    @SerialName("history_id") val historyId: Int,
    @SerialName("history_name") val historyName: String,
    @Serializable(with = LocalDateIso8601Serializer::class)
    val date: LocalDate,
    val route: String
)

data class HistoryData(
    val historyId: Int,
    val historyName: String,
    val date: LocalDate,
    val route: String
)

fun HistoryDataResponse.toHistoryData() = HistoryData(historyId, historyName, date, route)