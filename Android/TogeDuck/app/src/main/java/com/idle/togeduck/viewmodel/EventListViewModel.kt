package com.idle.togeduck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor() : ViewModel() {
    // 더미 데이터
    private val tempEventList = mutableListOf(
        Event("imageUrl1", "cafe1", "event1", "2024.01.02 ~ 2024.01.06", true),
        Event("imageUrl2", "cafe2", "event1", "2024.01.02 ~ 2024.01.05", true),
        Event("imageUrl3", "cafe3", "event1", "2024.01.03 ~ 2024.01.03", true),
        Event("imageUrl4", "cafe4", "event1", "2024.01.04 ~ 2024.01.10", true),
        Event("imageUrl5", "cafe5", "event1", "2023.12.30 ~ 2024.01.02", true),
        Event("imageUrl6", "cafe6", "event1", "2024.01.02 ~ 2024.02.06", true),
        Event("imageUrl7", "cafe7", "event1", "2024.02.02 ~ 2024.02.04", true),
        Event("imageUrl8", "cafe8", "event1", "2024.01.01 ~ 2024.01.16", true),
        Event("imageUrl9", "cafe9", "event1", "2024.01.06 ~ 2024.01.06", true),
        Event("imageUrl10", "cafe10", "event1", "2024.01.06 ~ 2024.01.08", true),
        Event("imageUrl11", "cafe11", "event1", "2024.01.07 ~ 2024.01.09", true),
        Event("imageUrl12", "cafe12", "event1", "2024.01.04 ~ 2024.01.06", true),
        Event("imageUrl13", "cafe13", "event1", "2024.01.05 ~ 2024.01.06", true)
    )

    private val _eventList = MutableLiveData<MutableList<Event>>()
    val eventList: LiveData<MutableList<Event>>
        get() = _eventList

    init {
        _eventList.value = tempEventList
    }

}