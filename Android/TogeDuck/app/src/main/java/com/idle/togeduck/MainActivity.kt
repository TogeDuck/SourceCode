package com.idle.togeduck

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.idle.togeduck.databinding.ActivityMainBinding
import com.idle.togeduck.util.CalcNavigationBarSize.getNavigationBarHeightToPx
import com.idle.togeduck.util.ScreenSize.heightDp
import com.idle.togeduck.util.ScreenSize.heightPx
import com.idle.togeduck.util.ScreenSize.widthDp
import com.idle.togeduck.util.ScreenSize.widthPx
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
    }

    // 상태바 투명하게 하는 기능
    private fun Activity.setStatusBarTransparent() {
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        if (Build.VERSION.SDK_INT >= 30) {    // API 30 에 적용
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }
}