package com.idle.togeduck.network

import android.content.ContentValues
import android.util.Log
import com.google.gson.Gson
import com.idle.togeduck.websocketcustomlibrary.Stomp
import com.idle.togeduck.websocketcustomlibrary.StompClient
import com.idle.togeduck.websocketcustomlibrary.dto.StompHeader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class StompManagerTest {
    // StompClient의 인스턴스를 생성할 때 사용할 URL
//    private val SERVER_URL = "ws://10.0.2.2:8080/ws-stomp"
    private val SERVER_URL = "wss://i10a301.p.ssafy.io/ws-stomp"

    private val stompClient: StompClient
    private val compositeDisposable = CompositeDisposable()

    init {
        val headerMap: Map<String, String> = mapOf(
            Pair("Authorization","Dddd")
        )
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SERVER_URL, headerMap)
        configureStompClient(stompClient)
    }

    private fun configureStompClient(stompClient: StompClient) {
        // stompClient에 대한 추가적인 설정을 할 수 있습니다.
        // 예를 들어, heartbeat을 설정하거나, 헤더를 추가할 수 있습니다.
    }

    fun connect(headers: List<StompHeader>) {
        stompClient.connect(headers)
    }
    fun connect() {
        stompClient.connect()
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
                Log.e(ContentValues.TAG, "Error on subscribe topic", throwable)
            })
        compositeDisposable.add(disposable) // 새로운 구독을 CompositeDisposable에 추가
    }

    fun clearSubscriptions() {
        compositeDisposable.clear() // 모든 구독 해제
    }

}
