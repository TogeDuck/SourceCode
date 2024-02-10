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
        stompClient.withClientHeartbeat(10000).withServerHeartbeat(10000)
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

    fun send(destination: String, message: MessageRequest) {
        stompClient.send(destination, Gson().toJson(message), headers).subscribe()
    }
    fun sendChat(chatId: Long, message:String){
        Log.d("웹소켓 헤더", headers.toString())
        val destination = "/pub/chats/$chatId/message"
        stompClient.send(destination, Gson().toJson(MessageRequest(chatId, message)), headers).subscribe()
        Log.d("웹소켓 전송", destination+" "+Gson().toJson(MessageRequest(chatId, message)))
    }
    fun sendLocation(celebrityId: Long, lat:Double, lng:Double, userId:String, type:String){
        Log.d("웹소켓 헤더", headers.toString())
        val destination = "/pub/celebrities/$celebrityId/message"
        stompClient.send(destination, Gson().toJson(CoordinateRequest(celebrityId,lat,lng,userId,type)), headers).subscribe()
        Log.d("웹소켓 전송", destination+" "+Gson().toJson(CoordinateRequest(celebrityId,lat,lng,userId,type)))
    }

    private fun subscribeTopic(topic: String, onMessageReceived: (String) -> Unit) {
        val disposable = stompClient.topic(topic)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                Log.d("웹소켓 메세지 수신", "토픽 ($topic) : 메세지 ($topicMessage)")
                onMessageReceived(topicMessage.payload)
            }, { throwable ->
                Log.e(ContentValues.TAG, "Error on"+topic, throwable)
            })
        compositeDisposable.add(disposable)
        topicSubscriptions[topic] = disposable
        Log.d("웹소켓", "토픽 $topic 구독 시작")
    }
    // 채팅 구독
    fun subscribeChat(chatId: Long, onMessageReceived: (String) -> Unit) {
        val topic = "/sub/chats/$chatId"
        subscribeTopic(topic,onMessageReceived)
    }
    // 실시간 사용자 위치 정보 구독
    fun subscribeCelebrity(celebrityId: Long, onMessageReceived: (String) -> Unit) {
        val topic = "/sub/celebrities/$celebrityId"
        subscribeTopic(topic,onMessageReceived)
    }

    fun unsubscribeTopic(topic: String) {
        topicSubscriptions[topic]?.let { disposable ->
            disposable.dispose()
            compositeDisposable.remove(disposable)
            topicSubscriptions.remove(topic)
        }
        stompClient.unsubscribePath(topic)
        Log.d("웹소켓", "토픽 $topic 구독 해제")
    }
    fun unsubscribeChat(chatId: Long){
        val topic = "/sub/chats/$chatId"
        unsubscribeTopic(topic)
    }
    fun unsubscribeCelebrity(celebrityId: Long){
        val topic = "/sub/celebrities/$celebrityId"
        unsubscribeTopic(topic)
    }
    fun clearSubscriptions() {
        compositeDisposable.clear()
    }
    fun disconnect() {
        stompClient.disconnect()
        clearSubscriptions()
    }
}
