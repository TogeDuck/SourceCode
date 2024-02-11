package com.idle.togeduck.quest.recruit

import android.os.Build
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.QuestType
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.quest.recruit.model.Recruit
import com.idle.togeduck.quest.recruit.model.RecruitRepository
import com.idle.togeduck.quest.recruit.model.RecruitRequest
import com.idle.togeduck.quest.recruit.model.recruitResponseToRecruit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RecruitViewModel @Inject constructor(
    private val recruitRepository: RecruitRepository
) : ViewModel() {
    private val _recruitList = MutableLiveData<List<Recruit>>()

    @Inject
    lateinit var stompManager: StompManager
    val recruitList: LiveData<List<Recruit>>
        get() = _recruitList

    var needUpdate: MutableLiveData<Boolean> = MutableLiveData(false)

    suspend fun getRecruitList(eventId: Long, page: Int, size: Int){
        val responseResult = recruitRepository.getRecruitList(eventId, page, size)

        if(responseResult.isSuccessful){
            val body = responseResult.body()!!
            _recruitList.postValue(body.data.content.map { it.recruitResponseToRecruit() })
            Log.d("모집 조회 완료",body.data.content.toString())
        }else{
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "RecruitViewModel - getRecruitList() 응답 실패 - $errorBody")
        }
    }

    suspend fun createRecruit(eventId: Long, recruitRequest: RecruitRequest, celebrityId:Long) {
        val responseResult = recruitRepository.createRecruit(eventId, recruitRequest)
        Log.d("로그", "RecruitViewModel - createRecruit() 호출됨 - ${responseResult}")

        if (!responseResult.isSuccessful) {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "RecruitViewModel - createRecruit() 응답 실패 - $errorBody")
        }
        else{
            // 웹소켓 교환 발생 알림 전송
            delay(1000)
            stompManager.sendQuestAlert(QuestType.GROUP.toString(),eventId,celebrityId)
        }
    }

    fun removeItem(recruit: Recruit){
        val currentList = _recruitList.value?.toMutableList() ?: mutableListOf()
        currentList.remove(recruit)
        _recruitList.value = currentList
    }


}