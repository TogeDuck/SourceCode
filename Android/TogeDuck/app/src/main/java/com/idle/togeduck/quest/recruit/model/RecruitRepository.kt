package com.idle.togeduck.quest.recruit.model

import retrofit2.Response
import javax.inject.Inject

interface PartyRepository {
    suspend fun getPartyList(
        page: Int,
        size: Int,
    ): Response<RecruitListResponse>
}

class DefaultPartyRepository @Inject constructor(
    private val partyService: RecruitService,
) : PartyRepository {
    override suspend fun getPartyList(
        page: Int,
        size: Int
    ): Response<RecruitListResponse> {
        return partyService.getPartyList(page, size)
    }
}