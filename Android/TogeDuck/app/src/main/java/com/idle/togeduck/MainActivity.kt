package com.idle.togeduck

import android.app.Activity
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.activityViewModels
import com.idle.togeduck.common.ScreenSize.heightDp
import com.idle.togeduck.common.ScreenSize.heightPx
import com.idle.togeduck.common.ScreenSize.widthDp
import com.idle.togeduck.common.ScreenSize.widthPx
import com.idle.togeduck.databinding.ActivityMainBinding
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.quest.talk.TalkViewModel
import com.idle.togeduck.util.CalcNavigationBarSize.getNavigationBarHeightToPx
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val talkViewModel: TalkViewModel by viewModels()

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

        talkViewModel.getChatPreference()
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

    override fun onStop() {
        super.onStop()
        Log.d("로그", "MainActivity - onStop() 호출됨")
        talkViewModel.setChatPreference()
    }
    override fun onDestroy() {
        super.onDestroy()
//        talkViewModel.setChatPreference()
    }
}