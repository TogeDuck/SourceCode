package com.idle.togeduck.quest.share.model

import android.util.Log
import com.idle.togeduck.common.model.DefaultResponse
import okhttp3.MultipartBody
import retrofit2.Call
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

interface ShareService {

    // Header : Authorization 추가 (Bearer {JWt_TOKEN} 추가

    @GET("events/{eventId}/shares")
    suspend fun getShareList(
        @Path("eventId") eventId: Long,
        @Query("page") page: Int,
        @Query("size") size:Int
    ): Response<ShareListResponse>

    @Multipart
    @POST("events/{eventId}/shares")
    suspend fun createShare(
        @Path("eventId") eventId: Long,
        @Part image: MultipartBody.Part,
        @Part shareRequestDto: MultipartBody.Part
    ): Response<DefaultResponse>

    @PATCH("events/{eventId}/shares/{shareId}")
    suspend fun updateShare(
        @Path("eventId") eventId: Long,
        @Path("shareId") shareId: Long,
        @Body body: ShareRequest
    ): Response<DefaultResponse>

    @DELETE("events/{eventId}/shares/{shareId}")
    suspend fun deleteShare(
        @Path("eventId") eventId: Long,
        @Path("shareId") shareId: Long
    ): Response<DefaultResponse>
}