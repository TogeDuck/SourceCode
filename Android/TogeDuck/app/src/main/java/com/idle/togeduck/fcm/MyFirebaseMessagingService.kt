package com.idle.togeduck.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.idle.togeduck.MainActivity
import com.idle.togeduck.R
import com.idle.togeduck.di.MainApplication
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // token 을 서버로 전송
        Log.d("FCM", "MyFirebaseMessagingService - onNewToken() 호출됨 ${token}")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // TODO. 수신한 메시지를 처리
        Log.d("로그", "MyFirebaseMessagingService - onMessageReceived() ${message.notification!!.title}")
        Log.d("로그", "MyFirebaseMessagingService - onMessageReceived() ${message.notification!!.body}")
        Log.d("로그", "MyFirebaseMessagingService - onMessageReceived() ${message.data["dealId"]}")

        val type = message.data["type"]!!

        when (type) {
            "request" -> FCMData.dealId.postValue(message.data["dealId"]?.toLong())
            "accept" -> FCMData.isAccept.postValue(true)
            "reject" -> FCMData.isReject.postValue(true)
        }
    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}