package com.idle.togeduck.network

import android.util.Log
import com.google.gson.Gson
import com.idle.togeduck.di.PreferenceModule
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import javax.inject.Inject

class WebSocketManager @Inject constructor(private val preference: PreferenceModule) {
    val url = "wss://i10a301.p.ssafy.io/ws-stomp"
    val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
    var accessToken: String? = null
    val headerList = arrayListOf<StompHeader>()

    init {
        accessToken = runBlocking {
            preference.getAccessToken.first()
        }
        headerList.add(StompHeader("Authorization",accessToken))
    }

    fun connect(){
        Log.d("웹소켓",accessToken.toString())
        Log.d("웹소켓",headerList.toString())
        stompClient.connect(headerList)

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("웹소켓", "WebSocket Opened")
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.d("웹소켓", "WebSocket Closed")

                }
                LifecycleEvent.Type.ERROR -> {
                    Log.d("웹소켓", "WebSocket Error")
                    Log.d("웹소켓", "네트워크 오류 ${lifecycleEvent.exception.toString()}")
                    disconnect()
                }
                else ->{
                    Log.d("웹소켓", lifecycleEvent.message)
                }
            }
        }
    }

    fun subscribe(url: String, callback: (String) -> Unit) {
        try {
            stompClient.topic(url).subscribe { topicMessage ->
                val message = topicMessage.payload
                callback(message)
            }
        } catch (e: Exception) {
            Log.d("웹소켓", e.toString())
            disconnect()
        }
    }

    fun send(destination:String, chatId: Long, message: String){
        var messageRequest = Message(chatId, message)
        try {
            stompClient.send(destination, Gson().toJson(messageRequest))
        }
        catch (e: Exception){
            Log.d("웹소켓", "메세지 전송 실패")
        }
    }

    fun disconnect(){
        stompClient.disconnect()
    }
}