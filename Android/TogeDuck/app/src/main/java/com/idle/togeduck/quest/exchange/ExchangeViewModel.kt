package com.idle.togeduck.quest.exchange

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.togeduck.QuestType
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.quest.exchange.model.DefaultExchangeRepository
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.quest.exchange.model.toExchange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val exchangeRepository: DefaultExchangeRepository
) : ViewModel() {
    @Inject
    lateinit var stompManager: StompManager
    private val _exchangeList = MutableLiveData<List<Exchange>>()
    val exchangeList: LiveData<List<Exchange>>
        get() = _exchangeList

    private val _myExchangeList = MutableLiveData<List<Exchange>>()
    val myExchangeList: LiveData<List<Exchange>>
        get() = _myExchangeList


    private val _selectedExchange = MutableLiveData<Exchange>()
    val selectedExchange: LiveData<Exchange>
        get() = _selectedExchange

    private val _myselectedExchange = MutableLiveData<Exchange>()
    val mySelectedExchange: LiveData<Exchange>
        get() = _myselectedExchange

    private val _navigationEvent = MutableLiveData<Boolean>(false)
    val navigationEvent: LiveData<Boolean>
        get() = _navigationEvent

    var needUpdate: MutableLiveData<Boolean> = MutableLiveData(false)

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
        Log.d("내 교환 리스트 가져오기","응답"+response.toString())
        if(response.isSuccessful){
            val exchangeMyListResponse = response.body()
            val myExchanges = exchangeMyListResponse?.data?.content ?: emptyList()
            _myExchangeList.postValue(myExchanges.map { it.toExchange() })
        }
        else{
            Log.d("내 교환 리스트 가져오기", response.toString())
        }
    }

    suspend fun postExchange(eventId: Long, image:MultipartBody.Part, tradeRequestDto: MultipartBody.Part, celebrityId:Long){
        val responseResult = exchangeRepository.postExchange(eventId, image, tradeRequestDto)
        Log.d("로그", "ExchangeViewModel - postExchange() 호출됨 - ${responseResult}")

        if (!responseResult.isSuccessful) {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "ExchangeViewModel - postExchange() 응답 실패 - $errorBody")
        }
        else{
            // 웹소켓 교환 발생 알림 전송
            delay(1000)
            stompManager.sendQuestAlert(QuestType.EXCHANGE.toString(),eventId,celebrityId)
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

    suspend fun deleteExchange(eventId: Long, tradeId: Long) {
        val responseResult = exchangeRepository.deleteExchange(eventId, tradeId)
        Log.d("로그", "ExchangeViewModel - deleteExchange() 호출됨")

        if (!responseResult.isSuccessful) {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "ExchangeViewModel - deleteExchange() 응답 실패 - $errorBody")
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

    fun setMySelectedExchange(myExchange: Exchange){
        _myselectedExchange.value = myExchange
    }

    fun setNavigatjionEvent(){
        _navigationEvent.value = false
    }
}