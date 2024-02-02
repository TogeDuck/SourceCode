package com.idle.togeduck.quest.exchange.model

import com.idle.togeduck.common.model.DefaultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ExchangeService {
    @GET("events/{event_id}/trades")
    suspend fun getExchangeList(
        @Path("event_id") eventId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<ExchangeListResponse>

    @GET("events/{event_id}/trades/{trade_id}")
    suspend fun getExchangeDetail(
        @Path("event_id") eventId: Int,
        @Path("trade_id") tradeId: Int,
    ) : Response<ExchangeDetailResponse>

    @GET("events/{event_id}/my-trades")
    suspend fun getMyExchangeList(
        @Path("event_id") eventId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<ExchangeListResponse>

    @Multipart
    @POST("events/{event_id}/trades")
    suspend fun postExchange(
        @Path("event_id") eventId: Int,
        @Part image: MultipartBody.Part,
        @Part content: String,
        @Part duration: Int
    ) : Response<DefaultResponse>

    @Multipart
    @PATCH("events/{event_id}/trades/{trade_id}")
    suspend fun updateExchange(
        @Path("event_id") eventId: Int,
        @Path("trade_id") tradeId: Int,
        @Part image: MultipartBody.Part,
        @Part content: String,
        @Part duration: Int
    ) : Response<DefaultResponse>

    @DELETE("events/{event_id}/trades/{trade_id}")
    suspend fun deleteExchange(
        @Path("event_id") eventId: Int,
        @Path("trade_id") tradeId: Int,
    ) : Response<DefaultResponse>

    @POST("events/{event_id}/trades/{trade_id}/requests")
    suspend fun requestExchange(
        @Path("event_id") eventId: Int,
        @Path("trade_id") tradeId: Int,
    ) : Response<DefaultResponse>

    @POST("events/{event_id}/trades/{trade_id}/requests/accept")
    suspend fun requestAcceptExchange(
        @Path("event_id") eventId: Int,
        @Path("trade_id") tradeId: Int,
    ) : Response<DefaultResponse>

    @POST("events/{event_id}/trades/{trade_id}/requests/reject")
    suspend fun requestRejectExchange(
        @Path("event_id") eventId: Int,
        @Path("trade_id") tradeId: Int,
    ) : Response<DefaultResponse>
}