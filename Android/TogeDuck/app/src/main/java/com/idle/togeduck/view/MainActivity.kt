package com.idle.togeduck.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.idle.togeduck.databinding.ActivityMainBinding
import com.idle.togeduck.util.ScreenSize.heightDp
import com.idle.togeduck.util.ScreenSize.heightPx
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = resources.displayMetrics
        heightPx = displayMetrics.heightPixels
        heightDp = (displayMetrics.heightPixels / displayMetrics.density).toInt()

        this.setStatusBarTransparent()
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