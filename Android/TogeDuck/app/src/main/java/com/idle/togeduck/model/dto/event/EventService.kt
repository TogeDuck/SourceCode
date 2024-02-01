package com.idle.togeduck.model.dto.event

import com.idle.togeduck.model.dto.EventList
import kotlinx.datetime.LocalDate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventService {
    // LocalDate 오류 나면 String으로 변경 후 사용 필요
    @GET("events")
    suspend fun getEventList(
        @Query("celebrity-id") celebrityId: Int,
        @Query("start-date") startDate: LocalDate,
        @Query("end-date") endDate: LocalDate
    ): Response<EventList>

}