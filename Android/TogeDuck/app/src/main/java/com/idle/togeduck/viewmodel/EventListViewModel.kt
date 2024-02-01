package com.idle.togeduck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor() : ViewModel() {
    private val _eventList = MutableLiveData<MutableList<Event>>()
    val eventList: LiveData<MutableList<Event>>
        get() = _eventList

    init {

    }

}