package com.idle.togeduck.quest.exchange

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.idle.togeduck.R
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.quest.exchange.model.DefaultExchangeRepository
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.quest.exchange.model.ExchangeRequest
import com.idle.togeduck.quest.exchange.model.MyExchange
import com.idle.togeduck.quest.exchange.model.toExchange
import com.idle.togeduck.quest.share.model.Share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val exchangeRepository: DefaultExchangeRepository
) : ViewModel() {
    private val _exchangeList = MutableLiveData<List<Exchange>>()
    val exchangeList: LiveData<List<Exchange>>
        get() = _exchangeList

    private val _myExchangeList = MutableLiveData<List<MyExchange>>()
    val myExchangeList: LiveData<List<MyExchange>>
        get() = _myExchangeList


    private val _selectedExchange = MutableLiveData<Exchange>()
    val selectedExchange: LiveData<Exchange>
        get() = _selectedExchange

    private val _myselectedExchange = MutableLiveData<MyExchange>()
    val mySelectedExchange: LiveData<MyExchange>
        get() = _myselectedExchange

    private val _navigationEvent = MutableLiveData<Boolean>(false)
    val navigationEvent: LiveData<Boolean>
        get() = _navigationEvent


    init {
        _exchangeList.value = listOf()
        CoroutineScope(Dispatchers.IO).launch{
            getExchangeList(2, 1, 100)
        }
    }

    suspend fun getExchangeList(eventId: Long, page: Int, size: Int){
        val response = exchangeRepository.getExchangeList(eventId, page, size)
        Log.d("교환 로그", "ExchangeViewModel -  getExchangeList 호출됨")
        if (response.isSuccessful) {
            val exchangeListResponse = response.body()
            val exchanges = exchangeListResponse?.data?.content ?: emptyList()
            _exchangeList.postValue(exchanges.map{it.toExchange()})
            Log.d("교환 로그", "ExchangeViewModel -  getExchangeList 호출됨 - 응답 성공")
        } else {
            Log.d("교환 로그", "ExchangeViewModel -  getExchangeList 호출됨 - 응답 실패")
        }
    }

    suspend fun getMyExchangeList(eventId: Long){
        val response = exchangeRepository.getMyExchangeList(eventId)
        if(response.isSuccessful){
            val exchangeMyListResponse = response.body()
            val myExchanges = exchangeMyListResponse?.data?.content ?: emptyList()
            _myExchangeList.postValue(myExchanges.map { it.toExchange() })
        }
        else{

        }
    }

    suspend fun postExchange(eventId: Long, image:MultipartBody.Part, tradeRequestDto: MultipartBody.Part){
        val responseResult = exchangeRepository.postExchange(eventId, image, tradeRequestDto)
        Log.d("로그", "ExchangeViewModel - postExchange() 호출됨 - ${responseResult}")

        if (!responseResult.isSuccessful) {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "ExchangeViewModel - postExchange() 응답 실패 - $errorBody")
        }
    }

    suspend fun sendExchangeRequest(eventId: Long){
        val selectedExchangeValue = selectedExchange.value
        val mySelectedExchangeValue = mySelectedExchange.value

        if (selectedExchangeValue != null && mySelectedExchangeValue != null) {
            val response = exchangeRepository.requestExchange(eventId, selectedExchangeValue.id, mySelectedExchangeValue.id)
            if(response.isSuccessful){
                Log.d("교환","신청 완료")
                viewModelScope.launch {
                    _navigationEvent.value = true
                }
            }
        }
    }

    fun removeItemFromList(questExchange: Exchange) {
        val currentList = _exchangeList.value?.toMutableList() ?: mutableListOf()
        currentList.remove(questExchange)
        _exchangeList.value = currentList
    }

    fun setSelectedExchange(exchange: Exchange){
        _selectedExchange.value = exchange
    }

    fun setMySelectedExchange(myExchange: MyExchange){
        _myselectedExchange.value = myExchange
    }

    fun setNavigatjionEvent(){
        _navigationEvent.value = false
    }
}