package com.idle.togeduck.event.model

import com.idle.togeduck.common.model.DefaultResponse
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @GET("events")
    suspend fun getEventList(
        @Query("celebrity-id") celebrityId: Long,
        @Query("start-date") startDate: LocalDate,
        @Query("end-date") endDate: LocalDate,
    ): Response<EventListResponse>


    // TODO. 이벤트 정보 검색 추가 필요


    @GET("events/likes")
    suspend fun getLikesList(): Response<EventLikesDataResponse>

    @POST("events/likes")
    suspend fun likeEvent(
        @Body likeEventRequest: LikeEventRequest,
    ): Response<DefaultResponse>

    @DELETE("events/likes/{event_id}")
    suspend fun unlikeEvent(
        @Path("event_id") eventId: Long,
    ): Response<DefaultResponse>

    @Multipart
    @POST("events/{event_id}/reviews")
    suspend fun postReview(
        @Path("event_id") eventId: Long,
        @Part image: MultipartBody.Part?,
        @Part content: String,
    ): Response<DefaultResponse>

    @GET("events/{event_id}")
    suspend fun getReviewList(
        @Path("event_id") eventId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<EventReviewListResponse>

    @DELETE("events/{event_id}/reviews/{review_id}")
    suspend fun deleteReview(
        @Path("event_id") eventId: Long,
        @Path("review_id") reviewId: Long,
    ): Response<DefaultResponse>
}