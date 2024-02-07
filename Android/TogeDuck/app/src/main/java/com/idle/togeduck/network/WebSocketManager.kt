package com.idle.togeduck.network

import android.util.Log
import com.google.gson.Gson
import com.idle.togeduck.di.PreferenceModule
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import javax.inject.Inject

class WebSocketManager @Inject constructor(private val preference: PreferenceModule) {
//    val url = "wss://i10a301.p.ssafy.io/ws-stomp"
    val url = "ws://10.0.2.2:8080/ws-stomp"
    lateinit var stompClient : StompClient
    var accessToken: String? = null
    lateinit var headerMap: Map<String, String>
    private var isConnected = false

    init {
        headerMap = mapOf()
        GlobalScope.launch(Dispatchers.Main) {
            accessToken = withContext(Dispatchers.IO) {
                preference.getAccessToken.first() // 비동기로 AccessToken을 가져오기 위해 Dispatchers.IO를 사용합니다.
            }
            accessToken?.let {
                headerMap = mapOf(
                    Pair("Authorization", it)
                )
            }

            // Stomp 클라이언트 초기화 및 연결
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url, headerMap)
        }
    }

    suspend fun connect(): Boolean {
        var result = false
        lifeCycleLog()
        try {
            stompClient.connect()
            isConnected = true
            result = true
        } catch (e: Exception) {
            isConnected = false
            result = false
        }
        return result
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
        Log.d("웹소켓 메세지 전송 시도", message)
        try {
            Log.d("메세지 목적지", destination)
            stompClient.send(destination, Gson().toJson(messageRequest)).subscribe()
        }
        catch (e: Exception){
            Log.d("웹소켓", "메세지 전송 실패")
        }
    }

    fun disconnect(){
        isConnected = false
        stompClient.disconnect()
    }

    fun getConnectedState(): Boolean{
        return isConnected
    }

    fun lifeCycleLog(){
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
}