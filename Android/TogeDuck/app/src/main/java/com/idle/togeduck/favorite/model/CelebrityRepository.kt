package com.idle.togeduck.favorite.model

import retrofit2.Response
import javax.inject.Inject

interface CelebrityRepository {
    suspend fun getCelebrities(
        keyword: String
    ): Response<CelebrityListResponse>
}

class DefaultCelebrityRespotitory @Inject constructor(
    private val celebrityService: CelebrityService
): CelebrityRepository {
    override suspend fun getCelebrities(
        keyword: String
    ): Response<CelebrityListResponse> {
        return celebrityService.getCelebrities(keyword)
    }
}