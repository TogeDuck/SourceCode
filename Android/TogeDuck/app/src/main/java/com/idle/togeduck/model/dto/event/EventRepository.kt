package com.idle.togeduck.model.dto.event

import com.idle.togeduck.model.dto.EventList
import kotlinx.datetime.LocalDate
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

interface EventRepository {
    suspend fun getEventList(
        celebrityId: Int,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Response<EventList>
}

class DefaultEventRepository @Inject constructor(
    private val eventService: EventService,
) : EventRepository {
    override suspend fun getEventist(
        celebrityId: Int,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Response<EventList> {
        return eventService.getEventList(celebrityId, startDate, endDate)
    }

}