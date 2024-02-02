package com.idle.togeduck.model.dto.party

import com.idle.togeduck.model.dto.PartyListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PartyService {
    @GET("events/{event_id}/parties")
    suspend fun getPartyList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PartyListResponse>
}

//  events/${event_id}/parties?page=1&size=10