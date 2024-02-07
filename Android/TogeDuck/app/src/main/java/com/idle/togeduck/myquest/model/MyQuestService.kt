package com.idle.togeduck.myquest.model

import retrofit2.Response
import retrofit2.http.GET

interface MyQuestService {
    @GET("/events/chats")
    suspend fun getMyQuestList(
    ): Response<MyQuestListResponse>
}