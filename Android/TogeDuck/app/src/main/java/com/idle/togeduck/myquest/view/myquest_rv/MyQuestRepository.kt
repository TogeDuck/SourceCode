package com.idle.togeduck.myquest.view.myquest_rv

import com.idle.togeduck.myquest.model.MyQuestListResponse
import com.idle.togeduck.myquest.model.MyQuestService
import retrofit2.Response
import javax.inject.Inject

interface MyQuestRepository {
    suspend fun getMyQuestList(
    ): Response<MyQuestListResponse>
}

class DefaultMyQuestRepository @Inject constructor(
    private val myQuestService: MyQuestService
): MyQuestRepository {
    override suspend fun getMyQuestList(): Response<MyQuestListResponse> {
        return myQuestService.getMyQuestList()
    }
}