package com.idle.togeduck.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.idle.togeduck.common.Theme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.CircleOverlay

fun CircleOverlay.builder(position: LatLng, radius: Double, context: Context, naverMap: NaverMap): CircleOverlay {
    val circle = CircleOverlay()

    circle.center = position
    circle.radius = radius
    circle.color = ContextCompat.getColor(context, Theme.theme.main500Trans20)
    circle.map = naverMap

    return circle
}