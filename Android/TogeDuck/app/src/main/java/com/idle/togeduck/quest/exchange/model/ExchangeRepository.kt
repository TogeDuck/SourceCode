package com.idle.togeduck.quest.exchange.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import javax.inject.Inject

interface ExchangeRepository {
    suspend fun getExchangeList(
        eventId: Int,
        page: Int,
        size: Int,
    ): Response<ExchangeListResponse>

    suspend fun getExchangeDetail(
        eventId: Int,
        tradeId: Int,
    ) : Response<ExchangeDetailResponse>

    suspend fun getMyExchangeList(
        eventId: Int,
        page: Int,
        size: Int
    ) : Response<ExchangeListResponse>

    suspend fun postExchange(
        eventId: Int,
        postExchangeRequest: PostExchangeRequest
    ) : Response<DefaultResponse>

    suspend fun updateExchange(
        eventId: Int,
        tradeId: Int,
        updateExchangeRequest: UpdateExchangeRequest
    ) : Response<DefaultResponse>

    suspend fun deleteExchange(
        eventId: Int,
        tradeId: Int,
    ) : Response<DefaultResponse>

    suspend fun requestExchange(
        eventId: Int,
        tradeId: Int,
    ) : Response<DefaultResponse>

    suspend fun requestAcceptExchange(
        eventId: Int,
        tradeId: Int,
    ) : Response<DefaultResponse>

    suspend fun requestRejectExchange(
        eventId: Int,
        tradeId: Int,
    ) : Response<DefaultResponse>
}

class DefaultExchangeRepository @Inject constructor(
    private val exchangeService: ExchangeService
) : ExchangeRepository {
    override suspend fun getExchangeList(
        eventId: Int,
        page: Int,
        size: Int,
    ): Response<ExchangeListResponse> {
        return exchangeService.getExchangeList(eventId, page, size)
    }

    override suspend fun getExchangeDetail(
        eventId: Int,
        tradeId: Int,
    ): Response<ExchangeDetailResponse> {
        return exchangeService.getExchangeDetail(eventId, tradeId)
    }

    override suspend fun getMyExchangeList(
        eventId: Int,
        page: Int,
        size: Int,
    ): Response<ExchangeListResponse> {
        return exchangeService.getMyExchangeList(eventId, page, size)
    }

    override suspend fun postExchange(
        eventId: Int,
        postExchangeRequest: PostExchangeRequest,
    ): Response<DefaultResponse> {
        return exchangeService.postExchange(eventId, postExchangeRequest)
    }

    override suspend fun updateExchange(
        eventId: Int,
        tradeId: Int,
        updateExchangeRequest: UpdateExchangeRequest,
    ): Response<DefaultResponse> {
        return exchangeService.updateExchange(eventId, tradeId, updateExchangeRequest)
    }

    override suspend fun deleteExchange(eventId: Int, tradeId: Int): Response<DefaultResponse> {
        return exchangeService.deleteExchange(eventId, tradeId)
    }

    override suspend fun requestExchange(eventId: Int, tradeId: Int): Response<DefaultResponse> {
        return exchangeService.requestExchange(eventId, tradeId)
    }

    override suspend fun requestAcceptExchange(
        eventId: Int,
        tradeId: Int,
    ): Response<DefaultResponse> {
        return exchangeService.requestAcceptExchange(eventId, tradeId)
    }

    override suspend fun requestRejectExchange(
        eventId: Int,
        tradeId: Int,
    ): Response<DefaultResponse> {
        return exchangeService.requestRejectExchange(eventId, tradeId)
    }
}