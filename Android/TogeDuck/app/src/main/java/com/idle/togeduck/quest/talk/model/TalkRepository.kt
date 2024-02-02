package com.idle.togeduck.quest.talk.model

import retrofit2.Response
import javax.inject.Inject

interface TalkRepository {
    suspend fun getTalkList(eventId: Int): Response<TalkListResponse>
}

class DefaultTalkRepository @Inject constructor(
    private val talkService: TalkService,
) : TalkRepository {
    override suspend fun getTalkList(eventId: Int): Response<TalkListResponse> {
        return talkService.getTalkList(eventId)
    }
}