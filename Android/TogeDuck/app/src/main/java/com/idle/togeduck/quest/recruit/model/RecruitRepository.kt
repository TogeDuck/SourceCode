package com.idle.togeduck.quest.recruit.model

import com.idle.togeduck.common.model.DefaultResponse
import retrofit2.Response
import javax.inject.Inject

interface RecruitRepository {
    suspend fun getRecruitList(
        eventId: Int,
        page: Int,
        size: Int,
    ): Response<RecruitListResponse>

    suspend fun createRecruit(
        eventId: Int,
        recruitRequest: RecruitRequest
    ): Response<DefaultResponse>

    suspend fun updateRecruit(
        eventId: Int,
        recruitId: Int,
        updateRequest: RecruitRequest
    ): Response<DefaultResponse>

    suspend fun deleteRecruit(
        eventId: Int,
        recruitId: Int,
    ): Response<DefaultResponse>

}

class DefaultRecruitRepository @Inject constructor(
    private val recruitService: RecruitService,
) : RecruitRepository {
    override suspend fun getRecruitList(
        eventId: Int,
        page: Int,
        size: Int
    ): Response<RecruitListResponse> {
        return recruitService.getRecruitList(eventId, page, size)
    }

    override suspend fun createRecruit(
        eventId: Int,
        recruitRequest: RecruitRequest
    ): Response<DefaultResponse> {
        return recruitService.createRecruit(eventId, recruitRequest)
    }

    override suspend fun updateRecruit(
        eventId: Int,
        recruitId: Int,
        updateRequest: RecruitRequest
    ): Response<DefaultResponse> {
        return recruitService.updateRecruit(eventId, recruitId, updateRequest)
    }

    override suspend fun deleteRecruit(
        eventId: Int,
        recruitId: Int
    ): Response<DefaultResponse> {
        return recruitService.deleteRecruit(eventId, recruitId)
    }
}