package com.idle.togeduck.quest.recruit.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecruitService {
    @GET("events/{event_id}/parties")
    suspend fun getPartyList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<RecruitListResponse>
}

//  events/${event_id}/parties?page=1&size=10