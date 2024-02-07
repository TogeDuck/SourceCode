package com.idle.togeduck.myquest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.myquest.model.MyQuest
import com.idle.togeduck.myquest.model.toMyQuest
import com.idle.togeduck.myquest.view.myquest_rv.MyQuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyQuestViewModel @Inject constructor(
    private val myQuestRepository: MyQuestRepository
) : ViewModel(){
    private val _myQuestList = MutableLiveData<List<MyQuest>>()
    val myQuestList: LiveData<List<MyQuest>> get() = _myQuestList

    private val _selectedMyQuest = MutableLiveData<MyQuest>()
    val selectedMyQuest: LiveData<MyQuest> get() = _selectedMyQuest

    suspend fun getMyQuestList(){
        val response = myQuestRepository.getMyQuestList()
        if(response.isSuccessful){
            val myQuestResponse = response.body()
            val exchanges = myQuestResponse?.data ?: emptyList()
            _myQuestList.postValue(exchanges.map { it.toMyQuest() })
        }
    }

    fun setSelected(myQuest: MyQuest){
        _selectedMyQuest.value = myQuest
    }
}