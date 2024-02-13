package com.idle.togeduck.common

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.idle.togeduck.MainActivity
import com.idle.togeduck.MainViewModel
import com.idle.togeduck.R
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.util.LoginUtil.guid
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ForcedFinishService : Service() {
    private val TAG = javaClass.simpleName
    private val channelId = "TOGEDUCK_NOTIFICATION_CHANNEL"
    private val notificationId = 43
    private var id: Long? = null
    private var guid: String? = null

    @Inject
    lateinit var stompManager: StompManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        // 클릭시 원래 화면으로 돌아오게 하려면 아래 설정 필요
        val intent = Intent(this, MainActivity::class.java)
            .setAction(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // 안드로이드 12 부터는 "or PendingIntent.FLAG_MUTABLE" 추가해야됨
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentText("TogeDuck 앱이 실행 중입니다")
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(notificationId, notification)
        } else {
            startForeground(notificationId, notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        id = intent.getLongExtra("id", 1L)
        guid = intent.getStringExtra("guid")
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        Log.d("로그", "ForcedFinishService - onTaskRemoved() 호출됨")

        // TODO. 투어 종료
        CoroutineScope(Dispatchers.IO).launch {
            val result = async {
                stompManager.sendTourEnd(id!!, guid!!)
            }

            if (result.await()) {
                Log.d("로그", "ForcedFinishService - onTaskRemoved() 호출됨2")
                stopSelf()
            }
        }
    }

    // notification channel 만드는 기능
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            channelId,
            "Set Notification",
            NotificationManager.IMPORTANCE_MIN
        )
        notificationChannel.enableLights(false)
        notificationChannel.enableVibration(false)
        notificationChannel.description = "TogeDuck 앱 백그라운드 실행 중 안내 알림"

        val notificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}