package com.idle.togeduck.history.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryService {
    // 방문 경로 조회
    @GET("users/history-event")
    suspend fun getHistoryList(
        @Query("celebrity-id") celebrityID: Long
    ): Response<HistoryListResponse>

    @POST("users/history-event")
    suspend fun addHistory(
        @Body addHistoryRequest: AddHistoryRequest
    ): Response<DefaultResponse>

    @GET("users/history")
    suspend fun createHistory(
        @Query("celebrity-id") celebrityID: Long
    ): Response<CreateHistoryResponse>

    // 방문 경로 상세 조회
    @GET("users/history/{history_id}")
    suspend fun getHistory(
        @Path("history_id") historyId: Long,
    ): Response<HistoryTourListResponse>


    @PATCH("users/history/{history_id}/name")
    suspend fun updateHistory(
        @Path("history_id") historyId: Long,
        @Body historyNameRequest: HistoryNameRequest
    ): Response<DefaultResponse>

    @PATCH("users/history/{history_id}/route")
    suspend fun sendHistory(
        @Path("history_id") historyId: Long,
        @Body sendHistoryRequest: SendHistoryRequest
    ): Response<DefaultResponse>

    @DELETE("users/history/{history_id}")
    suspend fun deleteHistory(
        @Path("history_id") historyId: Long,
    ): Response<DefaultResponse>
}