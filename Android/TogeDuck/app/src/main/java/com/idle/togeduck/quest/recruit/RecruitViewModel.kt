package com.idle.togeduck.quest.recruit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.idle.togeduck.quest.recruit.model.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecruitViewModel @Inject constructor(
    private val partyRepository: PartyRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getPartyList() {
        partyRepository.getPartyList(1, 10)
    }
}