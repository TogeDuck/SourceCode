package com.idle.togeduck.quest.exchange.model

import com.idle.togeduck.common.model.DefaultResponse
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import javax.inject.Inject

interface ExchangeRepository {
    suspend fun getExchangeList(
        eventId: Long,
        page: Int,
        size: Int,
    ): Response<ExchangeListResponse>

    suspend fun getExchangeDetail(
        eventId: Long,
        tradeId: Int,
    ) : Response<ExchangeDetailResponse>

    suspend fun getMyExchangeList(
        eventId: Long,
    ) : Response<ExchangeMyListResponse>

    suspend fun postExchange(
        eventId: Long,
        image: MultipartBody.Part,
        tradeRequestDto: MultipartBody.Part
    ) : Response<DefaultResponse>

    suspend fun updateExchange(
        eventId: Long,
        tradeId: Long,
        image: MultipartBody.Part,
        content: String,
        duration: Int
    ) : Response<DefaultResponse>

    suspend fun deleteExchange(
        eventId: Long,
        tradeId: Long,
    ) : Response<DefaultResponse>

    suspend fun requestExchange(
        eventId: Long,
        tradeId: Long,
        myTradeId: Long
    ) : Response<DefaultResponse>

    suspend fun requestAcceptExchange(
        eventId: Long,
        tradeId: Long,
    ) : Response<DefaultResponse>

    suspend fun requestRejectExchange(
        eventId: Long,
        tradeId: Long,
    ) : Response<DefaultResponse>

    suspend fun getExchangeQuestByDealId(dealId: Long) : Response<ExchangeRequestedResponse>

    suspend fun rejectExchange(dealId: Long) : Response<DefaultResponse>

    suspend fun acceptExchange(dealId: Long) : Response<DefaultResponse>
}

class DefaultExchangeRepository @Inject constructor(
    private val exchangeService: ExchangeService
) : ExchangeRepository {
    override suspend fun getExchangeList(
        eventId: Long,
        page: Int,
        size: Int,
    ): Response<ExchangeListResponse> {
        return exchangeService.getExchangeList(eventId, page, size)
    }

    override suspend fun getExchangeDetail(
        eventId: Long,
        tradeId: Int,
    ): Response<ExchangeDetailResponse> {
        return exchangeService.getExchangeDetail(eventId, tradeId)
    }

    override suspend fun getMyExchangeList(
        eventId: Long,
    ): Response<ExchangeMyListResponse> {
        return exchangeService.getMyExchangeList(eventId)
    }

    override suspend fun postExchange(
        eventId: Long,
        image: MultipartBody.Part,
        tradeRequestDto: MultipartBody.Part
    ): Response<DefaultResponse> {
        return exchangeService.postExchange(eventId, image, tradeRequestDto)
    }

    override suspend fun updateExchange(
        eventId: Long,
        tradeId: Long,
        image: MultipartBody.Part,
        content: String,
        duration: Int,
    ): Response<DefaultResponse> {
        return exchangeService.updateExchange(eventId, tradeId, image, content, duration)
    }

    override suspend fun deleteExchange(eventId: Long, tradeId: Long): Response<DefaultResponse> {
        return exchangeService.deleteExchange(eventId, tradeId)
    }

    override suspend fun requestExchange(eventId: Long, tradeId: Long, myTradeId: Long): Response<DefaultResponse> {
        return exchangeService.requestExchange(eventId, tradeId, myTradeId)
    }

    override suspend fun requestAcceptExchange(
        eventId: Long,
        tradeId: Long,
    ): Response<DefaultResponse> {
        return exchangeService.requestAcceptExchange(eventId, tradeId)
    }

    override suspend fun requestRejectExchange(
        eventId: Long,
        tradeId: Long,
    ): Response<DefaultResponse> {
        return exchangeService.requestRejectExchange(eventId, tradeId)
    }

    override suspend fun getExchangeQuestByDealId(dealId: Long): Response<ExchangeRequestedResponse> {
        return exchangeService.getExchangeQuestByDealId(dealId)
    }

    override suspend fun rejectExchange(dealId: Long): Response<DefaultResponse> {
        return exchangeService.rejectExchange(dealId)
    }

    override suspend fun acceptExchange(dealId: Long): Response<DefaultResponse> {
        return exchangeService.acceptExchange(dealId)
    }
}