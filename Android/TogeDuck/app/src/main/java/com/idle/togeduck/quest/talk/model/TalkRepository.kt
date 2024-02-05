package com.idle.togeduck.quest.talk.model

import retrofit2.Response
import javax.inject.Inject

interface TalkRepository {
    suspend fun getTalkList(eventId: Long): Response<TalkListResponse>
}

class DefaultTalkRepository @Inject constructor(
    private val talkService: TalkService,
) : TalkRepository {
    override suspend fun getTalkList(eventId: Long): Response<TalkListResponse> {
        return talkService.getTalkList(eventId)
    }
}