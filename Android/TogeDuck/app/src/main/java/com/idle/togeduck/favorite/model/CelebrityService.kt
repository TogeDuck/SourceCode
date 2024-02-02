package com.idle.togeduck.favorite.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CelebrityService {
    @GET("/celebrities/search")
    suspend fun getCelebrities(
        @Query("keyword") keyword: String
    ): Response<CelebrityListResponse>
}