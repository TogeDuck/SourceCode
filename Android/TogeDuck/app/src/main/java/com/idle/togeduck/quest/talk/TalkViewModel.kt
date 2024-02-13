package com.idle.togeduck.quest.talk

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.quest.talk.model.Talk
import com.idle.togeduck.quest.talk.model.TalkRepository
import com.idle.togeduck.quest.talk.model.TalkRoom
import com.idle.togeduck.quest.talk.model.toTalk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class TalkViewModel @Inject constructor(
    private val talkRepository: TalkRepository,
    private val preference: PreferenceModule
) : ViewModel() {
    private val _talkList = MutableLiveData<List<Talk>>()
    val talkList: LiveData<List<Talk>> get() = _talkList

    private val _chatRoomList = MutableLiveData<MutableMap<Long, TalkRoom>>()
    val chatRoomList: LiveData<MutableMap<Long, TalkRoom>> get() = _chatRoomList

    private val _chatRoomTalkList = MutableLiveData<MutableMap<Long, MutableList<Talk>>>()
    val chatRoomTalkList: LiveData<MutableMap<Long, MutableList<Talk>>> get() = _chatRoomTalkList

    var currentChatRoomId: MutableLiveData<Long> = MutableLiveData(0L)


    init {
        viewModelScope.launch {
//            getTalkList(1)
        }
    }

    fun clearTalkList(){
        _talkList.postValue(mutableListOf())
    }
    fun addTalk(talk: Talk) {
        val currentList = _talkList.value ?: emptyList()
        val updatedList = currentList + talk
        _talkList.postValue(updatedList)
    }
    fun addTalkRoom(talkRoom: TalkRoom){
        val chatRoomList = _chatRoomList.value ?: mutableMapOf()
        chatRoomList[talkRoom.chatId] = talkRoom
        _chatRoomList.postValue(chatRoomList)
    }
    fun addTalkRoomTalk(chatRoomId: Long, talk: Talk) {
        val updatedMap = _chatRoomTalkList.value?.toMutableMap() ?: mutableMapOf()
        val talksForRoom = updatedMap[chatRoomId]?.toMutableList() ?: mutableListOf()
        talksForRoom.add(talk)
        updatedMap[chatRoomId] = talksForRoom
        _chatRoomTalkList.postValue(updatedMap)
        Log.d("새로운 채팅 업데이트", talksForRoom.toString())
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

    suspend fun getChatRoomTalkList(chatRoomId: Long){
    }

    fun getChatPreference() {
        Log.d("로그", "TalkViewModel - getChatPreference() 호출됨")
        val chatRoomListPreference = runBlocking {
            preference.getChatRoomList.first()
        }

        val chatRoomTalkListPreference = runBlocking {
            preference.getChatRoomTalkList.first()
        }

        if (chatRoomListPreference != null) {
            _chatRoomList.postValue(chatRoomListPreference)
        }

        if (chatRoomTalkListPreference != null) {
            _chatRoomTalkList.postValue(chatRoomTalkListPreference)
        }
    }

    fun setChatPreference() {
        Log.d("로그", "TalkViewModel - setChatPreference() 호출됨")
        viewModelScope.launch {
            if (_chatRoomList.value != null) {
                preference.setChatRoomList(_chatRoomList.value!!)
            }

            if (_chatRoomTalkList.value != null) {
                preference.setChatRoomTalkList(_chatRoomTalkList.value!!)
            }
        }
    }
}