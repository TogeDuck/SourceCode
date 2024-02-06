package com.idle.togeduck.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.event.model.EventRepository
import com.idle.togeduck.event.model.EventReviewContent
import com.idle.togeduck.event.model.toEventReviewContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _reviewList = MutableLiveData<List<EventReviewContent>>()
    val reviewList: LiveData<List<EventReviewContent>>
        get() = _reviewList

    init {
        viewModelScope.launch {
            getReviewList(1,1,100)
        }
    }

    suspend fun postReview(eventId: Long, image: MultipartBody.Part, content: String) {
        val responseResult = eventRepository.postReview(eventId, image, content)

        if (!responseResult.isSuccessful) {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "EventListViewModel - postReview() 응답 실패 - $errorBody")
        }
    }

    suspend fun getReviewList(eventId: Long, page: Int, size: Int) {
        val responseResult = eventRepository.getReviewList(eventId, page, size)

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!
            _reviewList.postValue(body.data.content.map { it.toEventReviewContent() })
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "EventListViewModel - getReviewList() 응답 실패 - $errorBody")
        }
    }

    suspend fun deleteReview(eventId: Long, reviewId: Long) {
        val responseResult = eventRepository.deleteReview(eventId, reviewId)

        if (!responseResult.isSuccessful) {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "EventListViewModel - deleteReview() 응답 실패 - $errorBody")
        }
    }
}