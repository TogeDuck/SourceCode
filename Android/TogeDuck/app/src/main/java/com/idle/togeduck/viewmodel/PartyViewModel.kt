package com.idle.togeduck.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.idle.togeduck.model.dto.party.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PartyViewModel @Inject constructor(
    private val partyRepository: PartyRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getPartyList() {
        partyRepository.getPartyList(1, 10)
    }
}