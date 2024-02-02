package com.idle.togeduck.quest.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.quest.share.model.Share
import com.idle.togeduck.quest.share.model.ShareRepository
import com.idle.togeduck.quest.share.model.shareResponseToShare
import javax.inject.Inject

class ShareViewModel @Inject constructor(
    private val shareRepository: ShareRepository
) : ViewModel(){
    private val _shareList = MutableLiveData<List<Share>>()
    val shareList: LiveData<List<Share>>
        get() = _shareList

    suspend fun getShareList(eventId: Long, page: Int, size: Int){
        val response = shareRepository.getShareList(eventId, page, size)
        if(response.isSuccessful) {
            val shareListResponse = response.body()
            val exchanges = shareListResponse?.data?.content ?: emptyList()
            _shareList.postValue(exchanges.map { it.shareResponseToShare() })
        }
        else{

        }
    }
}