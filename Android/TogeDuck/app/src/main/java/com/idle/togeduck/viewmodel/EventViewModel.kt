package com.idle.togeduck.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.togeduck.model.dto.event.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDate
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getEventList() {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.getEventList(1, java.time.LocalDate.now().toKotlinLocalDate(), java.time.LocalDate.now().toKotlinLocalDate())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getEventList2() {
        eventRepository.getEventList(1, java.time.LocalDate.now().toKotlinLocalDate(), java.time.LocalDate.now().toKotlinLocalDate())
    }
}