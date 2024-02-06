package com.idle.togeduck.network

import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent

class WebSocketManager {
    val url = "ws://70.12.246.147:8080/ws"
    val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

    fun connect(){
        stompClient.connect()

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
        stompClient.topic(url).subscribe { topicMessage ->
            val message = topicMessage.payload
            callback(message)
        }
    }

    fun disconnect(){
        stompClient.disconnect()
    }
}