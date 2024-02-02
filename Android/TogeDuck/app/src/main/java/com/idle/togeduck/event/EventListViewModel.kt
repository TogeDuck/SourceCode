package com.idle.togeduck.event

import android.os.Build
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

    suspend fun getEventList(celebrityId: Int, startDate: LocalDate, endDate: LocalDate){
        val responseResult = eventRepository.getEventList(celebrityId, startDate, endDate)

        if(responseResult.isSuccessful){
            val body = responseResult.body()!!
            _listToday.value = body.today.map { it.toEvent() }
            _listUpcoming.value = body.later.map { it.toEvent() }
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
            _listPast.value = body.past.map { it.toEvent() }
            _listToday.value = body.today.map { it.toEvent() }
            _listUpcoming.value = body.later.map { it.toEvent() }
        }else{
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "EventListViewModel - getLikesList() 응답 실패 - $errorBody")
        }
    }

    suspend fun likeEvent(likeEventRequest: LikeEventRequest){
        val responseResult = eventRepository.likeEvent(likeEventRequest)

        if(!responseResult.isSuccessful){
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "EventListViewModel - likeEvent() 응답 실패 - $errorBody")
        }
    }

    suspend fun unlikeEvent(eventId: Int) {
        val responseResult = eventRepository.unlikeEvent(eventId)

        if (!responseResult.isSuccessful) {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "EventListViewModel - unlikeEvent() 응답 실패 - $errorBody")
        }
    }
}