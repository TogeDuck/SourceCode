package com.idle.togeduck.model.dto.party

import com.idle.togeduck.model.dto.PartyList
import retrofit2.Response
import javax.inject.Inject

interface PartyRepository {
    suspend fun getPartyList(
        page: Int,
        size: Int,
    ): Response<PartyList>
}

class DefaultPartyRepository @Inject constructor(
    private val partyService: PartyService,
) : PartyRepository {
    override suspend fun getPartyList(
        page: Int,
        size: Int
    ): Response<PartyList> {
        return partyService.getPartyList(page, size)
    }
}