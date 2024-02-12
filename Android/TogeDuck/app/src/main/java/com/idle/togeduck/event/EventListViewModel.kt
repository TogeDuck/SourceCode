package com.idle.togeduck.event

import android.os.Build
import android.os.Build.VERSION_CODES.P
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.event.model.DefaultEventRepository
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.event.model.LikeEventRequest
import com.idle.togeduck.event.model.toEvent
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.quest.recruit.model.recruitResponseToRecruit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val eventRepository : DefaultEventRepository
) : ViewModel() {
    private val _listToday= MutableLiveData<List<Event>>()
    val listToday: LiveData<List<Event>>
        get() = _listToday

    private val _listUpcoming= MutableLiveData<List<Event>>()
    val listUpcoming: LiveData<List<Event>>
        get() = _listUpcoming

    private val _listPast = MutableLiveData<List<Event>>()
    val listPast: LiveData<List<Event>>
        get() = _listPast

    private val _selectedEvent = MutableLiveData<Event>()
    val selectedEvent: LiveData<Event>
        get() = _selectedEvent

    val closeEvents = MutableLiveData<List<Event>>()

    var isDetailOpen: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        _listToday.value = listOf()
        _listPast.value = listOf()
        _listUpcoming.value = listOf()
    }

    fun initClostEvents(){
        closeEvents.value = listOf()
    }

    suspend fun getEventList(celebrityId: Long, startDate: LocalDate, endDate: LocalDate){
        val responseResult = eventRepository.getEventList(celebrityId, startDate, endDate)

        Log.d("로그", "EventListViewModel - getEventList() 호출됨")
        
        if(responseResult.isSuccessful){
            val body = responseResult.body()!!
            val data = body.data

            _listPast.postValue(data.past.map { it.toEvent() })
            _listToday.postValue(data.today.map { it.toEvent() })
            _listUpcoming.postValue(data.later.map { it.toEvent() })
            Log.d("로그", "EventListViewModel - getEventList() 응답 성공 $body" )
        }else{
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "EventListViewModel - getEventList() 응답 실패 - $errorBody")
        }
    }

    suspend fun getLikesList(){
        val responseResult = eventRepository.getLikesList()

        if(responseResult.isSuccessful){
            val body = responseResult.body()!!
            _listPast.postValue(body.past.map { it.toEvent() })
            _listToday.postValue(body.today.map { it.toEvent() })
            _listUpcoming.postValue(body.later.map { it.toEvent() })
        }else{
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "EventListViewModel - getLikesList() 응답 실패 - $errorBody")
        }
    }

    suspend fun likeEvent(eventId: Long){
        val responseResult = eventRepository.likeEvent(eventId)
        Log.d("로그", "EventListViewModel - likeEvent() 응답 성공 - $responseResult")

        if(!responseResult.isSuccessful){
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "EventListViewModel - likeEvent() 응답 실패 - $errorBody")
        }
    }

    suspend fun unlikeEvent(eventId: Long) {
        val responseResult = eventRepository.unlikeEvent(eventId)
        Log.d("로그", "EventListViewModel - unlikeEvent() 응답 성공 - $responseResult")

        if (!responseResult.isSuccessful) {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "EventListViewModel - unlikeEvent() 응답 실패 - $errorBody")
        }
    }

    fun setSelectedEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun clearList() {
        _listPast.value = listOf()
        _listToday.value = listOf()
        _listUpcoming.value = listOf()
    }

}