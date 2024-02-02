package com.idle.togeduck.my_record.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import javax.inject.Inject

interface HistoryRepository {

    suspend fun getHistoryList(
        celebrityId: Int
    ): Response<HistoryListResponse>

    suspend fun getHistory(
        historyId: Int,
        celebrityId: Int
    ): Response<HistoryTourListResponse>

    suspend fun updateHistory(
        historyId: Int,
        historyName: String
    ): Response<DefaultResponse>

    suspend fun deleteHistory(
        historyId: Int,
    ): Response<DefaultResponse>
}


class DefaultHistoryRepository @Inject constructor(
    private val historyService: HistoryService,
) : HistoryRepository {

    override suspend fun getHistoryList(
        celebrityId: Int
    ): Response<HistoryListResponse> {
        return historyService.getHistoryList(celebrityId)
    }

    override suspend fun getHistory(
        historyId: Int,
        celebrityId: Int
    ): Response<HistoryTourListResponse> {
        return historyService.getHistory(historyId, celebrityId)
    }

    override suspend fun updateHistory(
        historyId: Int,
        historyName: String
    ): Response<DefaultResponse> {
        return historyService.updateHistory(historyId, historyName)
    }

    override suspend fun deleteHistory(
        historyId: Int
    ): Response<DefaultResponse> {
        return historyService.deleteHistory(historyId)
    }
}