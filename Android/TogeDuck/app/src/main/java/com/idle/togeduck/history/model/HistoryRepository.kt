package com.idle.togeduck.history.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import javax.inject.Inject

interface HistoryRepository {

    suspend fun getHistoryList(
        historyRequest: HistoryRequest
    ): Response<HistoryListResponse>

    suspend fun getHistory(
        historyId: Long,
        historyRequest: HistoryRequest
    ): Response<HistoryTourListResponse>

    suspend fun updateHistory(
        historyId: Long,
        historyNameRequest: HistoryNameRequest
    ): Response<DefaultResponse>

    suspend fun deleteHistory(
        historyId: Long,
    ): Response<DefaultResponse>
}

class DefaultHistoryRepository @Inject constructor(
    private val historyService: HistoryService,
) : HistoryRepository {

    override suspend fun getHistoryList(
        historyRequest: HistoryRequest
    ): Response<HistoryListResponse> {
        return historyService.getHistoryList(historyRequest)
    }

    override suspend fun getHistory(
        historyId: Long,
        historyRequest: HistoryRequest
    ): Response<HistoryTourListResponse> {
        return historyService.getHistory(historyId, historyRequest)
    }

    override suspend fun updateHistory(
        historyId: Long,
        historyNameRequest: HistoryNameRequest
    ): Response<DefaultResponse> {
        return historyService.updateHistory(historyId, historyNameRequest)
    }

    override suspend fun deleteHistory(
        historyId: Long
    ): Response<DefaultResponse> {
        return historyService.deleteHistory(historyId)
    }
}