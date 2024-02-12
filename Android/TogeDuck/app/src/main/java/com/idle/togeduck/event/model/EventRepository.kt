package com.idle.togeduck.event.model

import com.idle.togeduck.common.model.DefaultResponse
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Path
import javax.inject.Inject

interface EventRepository {
    suspend fun getEventList(
        celebrityId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Response<EventListResponse>

    suspend fun getLikesList(): Response<EventDataResponse>

    suspend fun getEventById(eventId: Long) : Response<SingleEventResponse>

    suspend fun likeEvent(eventId: Long): Response<DefaultResponse>

    suspend fun unlikeEvent(eventId: Long): Response<DefaultResponse>

    suspend fun postReview(
        eventId: Long,
        image: MultipartBody.Part?,
        content: MultipartBody.Part,
    ): Response<DefaultResponse>

    suspend fun getReviewList(
        eventId: Long,
        page: Int,
        size: Int,
    ): Response<EventReviewListResponse>

    suspend fun deleteReview(
        eventId: Long,
        reviewId: Long,
    ): Response<DefaultResponse>
}

class DefaultEventRepository @Inject constructor(
    private val eventService: EventService,
) : EventRepository {
    override suspend fun getEventList(
        celebrityId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Response<EventListResponse> {
        return eventService.getEventList(celebrityId, startDate, endDate)
    }

    override suspend fun getLikesList(): Response<EventDataResponse> {
        return eventService.getLikesList()
    }

    override suspend fun getEventById(eventId: Long): Response<SingleEventResponse> {
        return eventService.getEventById(eventId)
    }

    override suspend fun likeEvent(eventId: Long): Response<DefaultResponse> {
        return eventService.likeEvent(eventId)
    }

    override suspend fun unlikeEvent(eventId: Long): Response<DefaultResponse> {
        return eventService.unlikeEvent(eventId)
    }

    override suspend fun postReview(
        eventId: Long,
        image: MultipartBody.Part?,
        content: MultipartBody.Part,
    ): Response<DefaultResponse> {
        return eventService.postReview(eventId, image, content)
    }

//    override suspend fun postReview(
//        eventId: Long,
//        image: MultipartBody.Part?,
//        content: RequestBody,
//    ): Response<DefaultResponse> {
//        return eventService.postReview(eventId, image, content)
//    }

    override suspend fun getReviewList(
        eventId: Long,
        page: Int,
        size: Int,
    ): Response<EventReviewListResponse> {
        return eventService.getReviewList(eventId, page, size)
    }

    override suspend fun deleteReview(eventId: Long, reviewId: Long): Response<DefaultResponse> {
        return eventService.deleteReview(eventId, reviewId)
    }
}