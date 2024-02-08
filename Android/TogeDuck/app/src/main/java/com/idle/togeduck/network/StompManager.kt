package com.idle.togeduck.network

import android.content.ContentValues
import android.util.Log
import com.google.gson.Gson
import com.idle.togeduck.websocketcustomlibrary.Stomp
import com.idle.togeduck.websocketcustomlibrary.StompClient
import com.idle.togeduck.websocketcustomlibrary.dto.StompHeader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class StompManager {
    private val SERVER_URL = "wss://i10a301.p.ssafy.io/ws-stomp"

    private val stompClient: StompClient
    private val compositeDisposable = CompositeDisposable()
    private val topicSubscriptions = mutableMapOf<String, Disposable>()
    private var headers: List<StompHeader> = listOf()

    init {
        val headerMap: Map<String,String> = mapOf(Pair("Authorization", "guest"))
        headers = listOf(StompHeader("Authorization", "guest"))
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SERVER_URL, headerMap)
        configureStompClient(stompClient)
        clearSubscriptions()
    }

    private fun configureStompClient(stompClient: StompClient) {
        stompClient.withClientHeartbeat(1000).withServerHeartbeat(1000)
    }

    fun setHeader(accessToken: String){
        headers = listOf(StompHeader("Authorization", accessToken))
    }

    fun connect(headers: List<StompHeader>) {
        stompClient.connect(headers)
    }
    fun connect() {
        stompClient.connect(headers)
    }

    fun disconnect() {
        stompClient.disconnect()
        clearSubscriptions()
    }

    fun send(destination: String, chatId:Long, message: String) {
        var messageRequest = MessageRequest(chatId, message, )
        stompClient.send(destination, Gson().toJson(messageRequest), headers).subscribe()
    }

    fun subscribeTopic(topic: String, onMessageReceived: (String) -> Unit) {
        val disposable = stompClient.topic(topic)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                onMessageReceived(topicMessage.payload)
            }, { throwable ->
                Log.e(ContentValues.TAG, "Error on"+topic, throwable)
            })
        compositeDisposable.add(disposable)
        topicSubscriptions[topic] = disposable
    }

    fun unsubscribeTopic(topic: String) {
        topicSubscriptions[topic]?.let { disposable ->
            disposable.dispose()
            compositeDisposable.remove(disposable)
            topicSubscriptions.remove(topic)
        }
        stompClient.unsubscribePath(topic)
    }

    fun clearSubscriptions() {
        compositeDisposable.clear()
    }
}
