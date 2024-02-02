package com.idle.togeduck.event.model

import kotlinx.datetime.LocalDate
import retrofit2.Response
import javax.inject.Inject

interface EventRepository {
    suspend fun getEventList(
        celebrityId: Int,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Response<EventListResponse>
}

class DefaultEventRepository @Inject constructor(
    private val eventService: EventService,
) : EventRepository {
    override suspend fun getEventist(
        celebrityId: Int,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Response<EventListResponse> {
        return eventService.getEventList(celebrityId, startDate, endDate)
    }

}