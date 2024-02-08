package com.idle.togeduck.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.history.model.AddHistoryRequest
import com.idle.togeduck.history.model.HistoryData
import com.idle.togeduck.history.model.HistoryNameRequest
import com.idle.togeduck.history.model.HistoryRepository
import com.idle.togeduck.history.model.HistoryTour
import com.idle.togeduck.history.model.Position
import com.idle.togeduck.history.model.toHistoryData
import com.idle.togeduck.history.model.toHistoryTour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {
    private val _historyList = MutableLiveData<List<HistoryData>>()
    val historyList: LiveData<List<HistoryData>>
        get() = _historyList

    private val _selectedHistory = MutableLiveData<HistoryData>()
    val selectedHistory: LiveData<HistoryData> get() = _selectedHistory

    private val _route = MutableLiveData<List<Position>>()
    val route: LiveData<List<Position>> get() = _route

    private val _historyEventList = MutableLiveData<List<HistoryTour>>()
    val historyEventList: LiveData<List<HistoryTour>> get() = _historyEventList


    init {
        viewModelScope.launch {
            getHistoryList(2) // TODO. 실제 celebrityId 적용 필요
        }
    }
    
    suspend fun getHistoryList(celebrityId: Long) {
        val responseResult = historyRepository.getHistoryList(celebrityId)

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            Log.d("로그", "HistoryViewModel - getHistoryList() 응답 성공 - ${body}")

            _historyList.postValue(body.data.map { it.toHistoryData() })
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "HistoryViewModel - getHistoryList() 응답 실패 - $errorBody")
        }
    }

    suspend fun addHistory(eventId: Long, historyId: Long) {
        val responseResult = historyRepository.addHistory(AddHistoryRequest(eventId, historyId))

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            // TODO. 작업 추가 필요
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "HistoryViewModel - addHistory() 응답 실패 - ${errorBody}")
        }
    }

    suspend fun createHistory(celebrityId: Long) {
        val responseResult = historyRepository.createHistory(celebrityId)

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            // TODO. 작업 추가 필요
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "HistoryViewModel - createHistory() 응답 실패 - ${errorBody}")
        }
    }

    suspend fun getHistory(historyId: Long) {
        val responseResult = historyRepository.getHistory(historyId)

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!
            _route.postValue(body.data.route)
            _historyEventList.postValue(body.data.historyEvent.map { it.toHistoryTour() })
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "HistoryViewModel - getHistory() 응답 실패 - $errorBody")
        }
    }

    suspend fun updateHistory(historyId: Long, historyName: String) {
        val responseResult = historyRepository.updateHistory(historyId, HistoryNameRequest(historyName))

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            Log.d("로그", "HistoryViewModel - updateHistory() 응답 성공 - $body")
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "HistoryViewModel - updateHistory() 응답 실패 - $errorBody")
        }
    }

    suspend fun sendHistory(historyId: Long, historyList: List<Position>) {
        val responseResult = historyRepository.sendHistory(historyId, historyList)

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            Log.d("로그", "HistoryViewModel - historyList() 응답 성공 - $body")
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "HistoryViewModel - sendHistory() 응답 실패 - $errorBody")
        }
    }

    suspend fun deleteHistory(historyId: Long) {
        val responseResult = historyRepository.deleteHistory(historyId)

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            Log.d("로그", "HistoryViewModel - deleteHistory() 응답 성공 - $body")
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "HistoryViewModel - deleteHistory() 응답 실패 - $errorBody")
        }
    }

    fun setSelectedHistory(historyData: HistoryData) {
        _selectedHistory.value = historyData
    }
}