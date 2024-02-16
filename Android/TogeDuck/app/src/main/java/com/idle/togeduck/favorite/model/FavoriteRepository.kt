package com.idle.togeduck.favorite.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import javax.inject.Inject

interface FavoriteRepository {
    suspend fun getFavorites(
    ): Response<FavoriteListResponse>

    suspend fun updateFavorites(
        body: FavoriteRequest
    ): Response<DefaultResponse>
}

class DefaultFavoriteRepository @Inject constructor(
    private val favoriteService: FavoriteService
): FavoriteRepository{
    override suspend fun getFavorites(): Response<FavoriteListResponse> {
        return favoriteService.getFavorites()
    }

    override suspend fun updateFavorites(
        body: FavoriteRequest
    ): Response<DefaultResponse> {
        return favoriteService.updateFavorites(body)
    }
}