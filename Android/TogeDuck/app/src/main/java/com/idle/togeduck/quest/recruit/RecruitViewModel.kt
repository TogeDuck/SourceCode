package com.idle.togeduck.quest.recruit

import android.os.Build
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.quest.recruit.model.Recruit
import com.idle.togeduck.quest.recruit.model.RecruitRepository
import com.idle.togeduck.quest.recruit.model.recruitResponseToRecruit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RecruitViewModel @Inject constructor(
    private val recruitRepository: RecruitRepository
) : ViewModel() {
    private val _recruitList = MutableLiveData<List<Recruit>>()
    val recruitList: LiveData<List<Recruit>>
        get() = _recruitList

    suspend fun getRecruitList(eventId: Int, page: Int, size: Int){
        val responseResult = recruitRepository.getRecruitList(eventId, page, size)

        if(responseResult.isSuccessful){
            val body = responseResult.body()!!
            _recruitList.value = body.data.content.map { it.recruitResponseToRecruit() }
        }else{
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "RecruitViewModel - getRecruitList() 응답 실패 - $errorBody")
        }
    }



}