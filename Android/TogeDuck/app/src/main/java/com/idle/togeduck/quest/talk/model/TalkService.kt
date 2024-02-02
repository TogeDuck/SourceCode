package com.idle.togeduck.quest.talk.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TalkService {
    @GET("events/{event_id}/talk")
    suspend fun getTalkList(
        @Path("event_id") eventId: Int
    ): Response<TalkListResponse>
}