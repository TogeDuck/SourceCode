package com.idle.togeduck.util

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object CalcDistance {
    private const val R = 6372.8 * 1000

    fun isDistanceOk(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Boolean {
        return getDistance(lat1, lng1, lat2, lng2) <= 500
    }
    fun getDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Int{
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng1 - lng2)
        val a = sin(dLat / 2).pow(2.0) + sin(dLng / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (R * c).toInt()
    }
}