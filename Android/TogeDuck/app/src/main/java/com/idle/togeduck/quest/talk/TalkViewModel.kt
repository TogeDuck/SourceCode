package com.idle.togeduck.quest.talk

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.quest.talk.model.Talk
import com.idle.togeduck.quest.talk.model.TalkRepository
import com.idle.togeduck.quest.talk.model.toTalk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class TalkViewModel @Inject constructor(
    private val talkRepository: TalkRepository
) : ViewModel() {
    private val _talkList = MutableLiveData<List<Talk>>()
    val talkList: LiveData<List<Talk>> get() = _talkList

    init {
        viewModelScope.launch {
            getTalkList(1)
        }
    }

    suspend fun getTalkList(eventId: Long) {
        val responseResult = talkRepository.getTalkList(eventId)

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            _talkList.postValue(body.data.content.map { it.toTalk() })
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "TalkViewModel - getTalkList() 호출됨 - 응답 실패 - $errorBody")
        }
    }
}