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
    // StompClient의 인스턴스를 생성할 때 사용할 URL
//    private val SERVER_URL = "ws://10.0.2.2:8080/ws-stomp"
    private val SERVER_URL = "wss://i10a301.p.ssafy.io/ws-stomp"

    private val stompClient: StompClient
    private val compositeDisposable = CompositeDisposable()
    private val topicSubscriptions = mutableMapOf<String, Disposable>()

    init {
        val headerMap: Map<String, String> = mapOf(
            Pair("Authorization","Dddd")
        )
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SERVER_URL, headerMap)
        configureStompClient(stompClient)
    }

    private fun configureStompClient(stompClient: StompClient) {
        stompClient.withClientHeartbeat(1000).withServerHeartbeat(1000)
    }

    fun connect(headers: List<StompHeader>) {
        stompClient.connect(headers)
    }
    fun connect() {
        val headers = listOf(
            StompHeader("Authorization", "guest")
        )
        stompClient.connect(headers)
    }

    fun disconnect() {
        stompClient.disconnect()
        compositeDisposable.clear() // 모든 구독 해제
    }

    fun send(destination: String, chatId:Long, message: String) {
        var messageRequest = Message(chatId, message)
        stompClient.send(destination, Gson().toJson(messageRequest)).subscribe()
    }

    fun send(destination: String, chatId:Long, message: String, headers: List<StompHeader>){
        var messageRequest = Message(chatId, message)
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
            disposable.dispose() // RxJava 구독 해제
            compositeDisposable.remove(disposable) // CompositeDisposable에서 제거
            topicSubscriptions.remove(topic) // Map에서 제거
        }
        // STOMP 프로토콜을 통해 서버에 구독 취소를 알립니다.
        stompClient.unsubscribePath(topic)
    }

    fun clearSubscriptions() {
        compositeDisposable.clear() // 모든 구독 해제
    }

}
