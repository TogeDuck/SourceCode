package com.idle.togeduck.quest.recruit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.idle.togeduck.quest.recruit.model.RecruitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecruitViewModel @Inject constructor(
    private val recruitRepository: RecruitRepository
) : ViewModel() {

//    @RequiresApi(Build.VERSION_CODES.O)
//    private suspend fun getPartyList() {
//        recruitRepository.getRecruitList(1, 10)
//    }
}