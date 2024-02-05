package com.idle.togeduck.quest.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.quest.exchange.model.DefaultExchangeRepository
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.quest.exchange.model.toExchange
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val exchangeRepository: DefaultExchangeRepository
) : ViewModel() {
    private val _exchangeList = MutableLiveData<List<Exchange>>()
    val exchangeList: LiveData<List<Exchange>>
        get() = _exchangeList

    suspend fun getExchangeList(eventId: Long, page: Int, size: Int){
        val response = exchangeRepository.getExchangeList(eventId, page, size)
        if (response.isSuccessful) {
            val exchangeListResponse = response.body()
            val exchanges = exchangeListResponse?.data?.content ?: emptyList()
            _exchangeList.postValue(exchanges.map{it.toExchange()})
        } else {

        }
    }
}