package com.idle.togeduck.quest.share.model

import com.idle.togeduck.common.model.DefaultResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Part
import javax.inject.Inject

interface ShareRepository {
    suspend fun getShareList(
       eventId: Long,
       page: Int,
       size: Int
    ): Response<ShareListResponse>

    suspend fun createShare(
        eventId: Long,
        image: MultipartBody.Part,
        shareRequestDto: MultipartBody.Part
    ): Response<DefaultResponse>

    suspend fun updateShare(
        eventId: Long,
        shareId: Long,
        body: ShareRequest
    ): Response<DefaultResponse>

    suspend fun deleteShare(
        eventId: Long,
        shareId: Long
    ): Response<DefaultResponse>
}

class DefaultShareRepository @Inject constructor(
    private val shareService: ShareService
): ShareRepository {
    override suspend fun getShareList(
        eventId: Long,
        page: Int,
        size: Int
    ):Response<ShareListResponse> {
        return shareService.getShareList(eventId, page, size)
    }

    override suspend fun createShare(
        eventId: Long,
        image: MultipartBody.Part,
        shareRequestDto: MultipartBody.Part
    ): Response<DefaultResponse> {
        return shareService.createShare(eventId, image, shareRequestDto)
    }

    override suspend fun updateShare(
        eventId: Long,
        shareId: Long,
        body: ShareRequest
    ): Response<DefaultResponse> {
        return  shareService.updateShare(eventId, shareId, body)
    }

    override suspend fun deleteShare(
        eventId: Long,
        shareId: Long
    ): Response<DefaultResponse> {
        return shareService.deleteShare(eventId, shareId)
    }
}