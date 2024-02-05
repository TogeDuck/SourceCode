package com.idle.togeduck.quest.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.quest.exchange.model.DefaultExchangeRepository
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.quest.exchange.model.MyExchange
import com.idle.togeduck.quest.exchange.model.toExchange
import com.idle.togeduck.quest.share.model.Share
import dagger.hilt.android.lifecycle.HiltViewModel
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

    suspend fun getExchangeList(eventId: Long, page: Int, size: Int){
        val response = exchangeRepository.getExchangeList(eventId, page, size)
        if (response.isSuccessful) {
            val exchangeListResponse = response.body()
            val exchanges = exchangeListResponse?.data?.content ?: emptyList()
            _exchangeList.postValue(exchanges.map{it.toExchange()})
        } else {

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
}