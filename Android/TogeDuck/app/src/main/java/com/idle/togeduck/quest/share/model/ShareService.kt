package com.idle.togeduck.quest.share.model

import com.google.android.gms.common.api.Response
import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShareService {

    // Header : Authorization 추가 (Bearer {JWt_TOKEN}

    @GET("events/{event_id}/shares")
    suspend fun getShareList(
        @Path("event_id") eventId: String,
        @Query("page") page: Int,
        @Query("size") size:Int
    ): Call<ShareListResponse>

    @POST("")
    suspend fun createShare(
        @Body body: Share
    ): Call<DefaultResponse>

    @PATCH("")
    suspend fun updateShare(

    ): Call<DefaultResponse>

    @DELETE("")
    suspend fun deleteShare(

    ): Call<DefaultResponse>
}