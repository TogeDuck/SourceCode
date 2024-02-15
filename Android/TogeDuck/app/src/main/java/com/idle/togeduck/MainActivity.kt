package com.idle.togeduck

import android.app.Activity
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import com.idle.togeduck.common.ForcedFinishService
import com.idle.togeduck.common.ScreenSize.heightDp
import com.idle.togeduck.common.ScreenSize.heightPx
import com.idle.togeduck.common.ScreenSize.widthDp
import com.idle.togeduck.common.ScreenSize.widthPx
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ActivityMainBinding
import com.idle.togeduck.di.AppModule
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.fcm.FCMData
import com.idle.togeduck.fcm.FCMData.dealId
import com.idle.togeduck.history.HistoryViewModel
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.main_map.view.MapFragment
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.talk.TalkViewModel
import com.idle.togeduck.util.CalcNavigationBarSize.getNavigationBarHeightToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val talkViewModel: TalkViewModel by viewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val mapViewModel: MapViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()
    private val exchangeViewModel: ExchangeViewModel by viewModels()

    @Inject
    lateinit var preference: PreferenceModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarTransparent()

        val displayMetrics = resources.displayMetrics
        heightPx = displayMetrics.heightPixels
        heightDp = (displayMetrics.heightPixels / displayMetrics.density).toInt()
        widthPx = displayMetrics.widthPixels
        widthDp = (displayMetrics.widthPixels / displayMetrics.density).toInt()

        binding.navHostFragment.setPadding(0, 0, 0, getNavigationBarHeightToPx(this))

        checkGPSSetting()

//        talkViewModel.getChatPreference()

        loadMyThemeAndCake()

        FCMData.dealId.observe(this) { dealId ->
            if (dealId != null) {
                receivedMessage(dealId)
            }
        }
    }

    private fun checkGPSSetting() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "TogeDuck 앱을 이용하시려면 GPS를 켜주세요", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    // 상태바 투명하게 하는 기능
    private fun Activity.setStatusBarTransparent() {
        window.apply {
//            setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }

        if (Build.VERSION.SDK_INT >= 30) {    // API 30 에 적용
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    private fun loadMyThemeAndCake() {
        val myTheme = runBlocking {
            preference.getTheme.first()
        }

        if (myTheme != null) {
            Theme.theme = myTheme
        } else {
            val newTheme = Theme.themeList.random()
            Theme.theme = newTheme
            CoroutineScope(Dispatchers.IO).launch {
                preference.setTheme(newTheme)
            }
        }

        val myCake = runBlocking {
            preference.getCakeCount.first()
        }

        if (myCake != null) {
            Theme.myCake = myCake
        } else {
            Theme.myCake = 0
            CoroutineScope(Dispatchers.IO).launch {
                preference.setCakeCount(0)
            }
        }
    }

    fun receivedMessage(dealId: Long?) {
        if (dealId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                exchangeViewModel.getQuestExchangeById(dealId)
            }
            binding.navHostFragment.findNavController().navigate(R.id.action_mapFragment_to_exchangeRequestedDialogFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        checkServiceRunning(0)
    }

    private fun checkServiceRunning(count: Int) {
        if (count < 5) {
            if (AppModule.isServiceRunning) {
                this.stopService(Intent(this, ForcedFinishService::class.java))
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(100)
                    checkServiceRunning(count + 1)
                }
            }
        }
    }
    override fun onStop() {
        super.onStop()
//        talkViewModel.setChatPreference()

        if (mapViewModel.isTourStart) {
            val intent = Intent(this, ForcedFinishService::class.java)
            intent.putExtra("id", favoriteSettingViewModel.selectedCelebrity.value?.id ?: 1)
            intent.putExtra("guid", mainViewModel.guid)
            startForegroundService(intent)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}