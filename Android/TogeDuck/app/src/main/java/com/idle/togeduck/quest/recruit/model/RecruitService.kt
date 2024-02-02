package com.idle.togeduck.quest.recruit.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecruitService {
    @GET("events/{event_id}/parties")
    suspend fun getRecruitList(
        @Path("event_id") eventId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<RecruitListResponse>

    @POST("events/{event_id}/parties")
    suspend fun createRecruit(
        @Path("event_id") eventId: Int,
        @Body recruitRequest: RecruitRequest
    ): Response<DefaultResponse>

    @PATCH("events/{event_id}/parties/{party_id}")
    suspend fun updateRecruit(
        @Path("event_id") eventId: Int,
        @Path("party_id") recruitId: Int,
        @Body updateRequest: RecruitRequest
    ): Response<DefaultResponse>

    @DELETE("/events/{event_id}/parties/{party_id}")
    suspend fun deleteRecruit(
        @Path("event_id") eventId: Int,
        @Path("party_id") recruitId: Int,
    ): Response<DefaultResponse>
}
