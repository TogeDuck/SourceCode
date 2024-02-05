package com.idle.togeduck.history.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryService {
    @GET("users/history")
    suspend fun getHistoryList(
        @Query("celebrity-id") historyRequest: HistoryRequest
    ): Response<HistoryListResponse>

    @GET("users/history/{history_id}")
    suspend fun getHistory(
        @Path("history_id") historyId: Long,
        @Body historyRequest: HistoryRequest
    ): Response<HistoryTourListResponse>

    @PATCH("users/history/{history_id}")
    suspend fun updateHistory(
        @Path("history_id") historyId: Long,
        @Body historyNameRequest: HistoryNameRequest
    ): Response<DefaultResponse>

    @DELETE("users/history/{history_id}")
    suspend fun deleteHistory(
        @Path("history_id") historyId: Long,
    ): Response<DefaultResponse>
}