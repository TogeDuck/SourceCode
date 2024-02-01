package com.idle.togeduck.model.dto.party

import com.idle.togeduck.model.dto.PartyListResponse
import retrofit2.Response
import javax.inject.Inject

interface PartyRepository {
    suspend fun getPartyList(
        page: Int,
        size: Int,
    ): Response<PartyListResponse>
}

class DefaultPartyRepository @Inject constructor(
    private val partyService: PartyService,
) : PartyRepository {
    override suspend fun getPartyList(
        page: Int,
        size: Int
    ): Response<PartyListResponse> {
        return partyService.getPartyList(page, size)
    }
}