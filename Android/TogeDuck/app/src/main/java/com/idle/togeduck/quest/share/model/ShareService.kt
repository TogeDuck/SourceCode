package com.idle.togeduck.quest.share.model

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

    @GET("events/{event_id}/shares")
    suspend fun getShareList(
        @Path("event_id") eventId: Long,
        @Query("page") page: Int,
        @Query("size") size:Int
    ): Response<ShareListResponse>

    @Multipart
    @POST("events/{event_id}/shares")
    suspend fun createShare(
        @Path("event_id") eventId: Long,
        @Part image: MultipartBody.Part,
        @Part shareRequestDto: MultipartBody.Part
    ): Response<DefaultResponse>

    @PATCH("events/{event_id}/shares/{share_id}")
    suspend fun updateShare(
        @Path("event_id") eventId: Long,
        @Path("share_id") shareId: Long,
        @Body body: ShareRequest
    ): Response<DefaultResponse>

    @DELETE("events/{event_id}/shares/{share_id}")
    suspend fun deleteShare(
        @Path("event_id") eventId: Long,
        @Path("share_id") shareId: Long
    ): Response<DefaultResponse>
}