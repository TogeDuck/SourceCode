package com.idle.togeduck.quest.share

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.quest.share.model.Share
import com.idle.togeduck.quest.share.model.ShareRepository
import com.idle.togeduck.quest.share.model.shareResponseToShare
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val shareRepository: ShareRepository
) : ViewModel(){
    private val _shareList = MutableLiveData<List<Share>>()
    val shareList: LiveData<List<Share>>
        get() = _shareList

    private val _selectedShare = MutableLiveData<Share>()
    val selectedShare: LiveData<Share>
        get() = _selectedShare

    suspend fun getShareList(eventId: Long, page: Int, size: Int){
        val response = shareRepository.getShareList(eventId, page, size)

        if(response.isSuccessful) {
            val shareListResponse = response.body()
            val exchanges = shareListResponse?.data?.content ?: emptyList()

            _shareList.postValue(exchanges.map { it.shareResponseToShare() })
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                response.errorBody()?.string()!!
            )
        }
    }

    fun setSelectedShare(share: Share) {
        _selectedShare.value = share
    }
}