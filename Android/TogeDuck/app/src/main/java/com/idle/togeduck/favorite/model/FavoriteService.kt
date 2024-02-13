package com.idle.togeduck.favorite.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface FavoriteService {
    @GET("/users/favorites")
    suspend fun getFavorites(
    ):Response<FavoriteListResponse>

    @PATCH("/users/favorites")
    suspend fun updateFavorites(
        @Body body: FavoriteRequest
    ):Response<DefaultResponse>
}