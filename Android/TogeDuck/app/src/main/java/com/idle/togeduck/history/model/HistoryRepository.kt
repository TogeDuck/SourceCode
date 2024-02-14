package com.idle.togeduck.history.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface HistoryRepository {

    suspend fun getHistoryList(
        celebrityId: Long
    ): Response<HistoryListResponse>

    suspend fun addHistory(
        addHistoryRequest: AddHistoryRequest
    ): Response<DefaultResponse>

    suspend fun createHistory(
        celebrityID: Long
    ): Response<CreateHistoryResponse>

    suspend fun getHistory(
        historyId: Long,
    ): Response<HistoryTourListResponse>

    suspend fun updateHistory(
        historyId: Long,
        historyNameRequest: HistoryNameRequest
    ): Response<DefaultResponse>

    suspend fun sendHistory(
        historyId: Long,
        sendHistoryRequest: SendHistoryRequest
    ): Response<DefaultResponse>

    suspend fun deleteHistory(
        historyId: Long,
    ): Response<DefaultResponse>
}

class DefaultHistoryRepository @Inject constructor(
    private val historyService: HistoryService,
) : HistoryRepository {

    override suspend fun getHistoryList(
        celebrityId: Long
    ): Response<HistoryListResponse> {
        return historyService.getHistoryList(celebrityId)
    }

    override suspend fun addHistory(addHistoryRequest: AddHistoryRequest): Response<DefaultResponse> {
        return historyService.addHistory(addHistoryRequest)
    }

    override suspend fun createHistory(celebrityID: Long): Response<CreateHistoryResponse> {
        return historyService.createHistory(celebrityID)
    }

    override suspend fun getHistory(
        historyId: Long,
    ): Response<HistoryTourListResponse> {
        return historyService.getHistory(historyId)
    }

    override suspend fun updateHistory(
        historyId: Long,
        historyNameRequest: HistoryNameRequest
    ): Response<DefaultResponse> {
        return historyService.updateHistory(historyId, historyNameRequest)
    }

    override suspend fun sendHistory(
        historyId: Long,
        sendHistoryRequest: SendHistoryRequest,
    ): Response<DefaultResponse> {
        return historyService.sendHistory(historyId, sendHistoryRequest)
    }

    override suspend fun deleteHistory(
        historyId: Long
    ): Response<DefaultResponse> {
        return historyService.deleteHistory(historyId)
    }
}