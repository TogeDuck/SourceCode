package com.idle.togeduck.event.model

import com.idle.togeduck.common.model.DefaultResponse
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

interface EventRepository {
    suspend fun getEventList(
        celebrityId: Int,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Response<EventListResponse>

    suspend fun getLikesList(): Response<EventLikesDataResponse>

    suspend fun likeEvent(likeEventRequest: LikeEventRequest): Response<DefaultResponse>

    suspend fun unlikeEvent(eventId: Int): Response<DefaultResponse>

    suspend fun postReview(
        eventId: Int,
        image: MultipartBody.Part,
        content: String,
    ): Response<DefaultResponse>

    suspend fun getReviewList(
        eventId: Int,
        page: Int,
        size: Int,
    ): Response<EventReviewListResponse>

    suspend fun deleteReview(
        eventId: Int,
        reviewId: Int,
    ): Response<DefaultResponse>
}

class DefaultEventRepository @Inject constructor(
    private val eventService: EventService,
) : EventRepository {
    override suspend fun getEventList(
        celebrityId: Int,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Response<EventListResponse> {
        return eventService.getEventList(celebrityId, startDate, endDate)
    }

    override suspend fun getLikesList(): Response<EventLikesDataResponse> {
        return eventService.getLikesList()
    }

    override suspend fun likeEvent(likeEventRequest: LikeEventRequest): Response<DefaultResponse> {
        return eventService.likeEvent(likeEventRequest)
    }

    override suspend fun unlikeEvent(eventId: Int): Response<DefaultResponse> {
        return eventService.unlikeEvent(eventId)
    }

    override suspend fun postReview(
        eventId: Int,
        image: MultipartBody.Part,
        content: String,
    ): Response<DefaultResponse> {
        return eventService.postReview(eventId, image, content)
    }

    override suspend fun getReviewList(
        eventId: Int,
        page: Int,
        size: Int,
    ): Response<EventReviewListResponse> {
        return eventService.getReviewList(eventId, page, size)
    }

    override suspend fun deleteReview(eventId: Int, reviewId: Int): Response<DefaultResponse> {
        return eventService.deleteReview(eventId, reviewId)
    }
}