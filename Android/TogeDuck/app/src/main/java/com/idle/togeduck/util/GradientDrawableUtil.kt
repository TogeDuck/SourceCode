package com.idle.togeduck.util

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

@ColorInt
fun getColor(context: Context, @ColorRes id: Int) : Int {
    return ContextCompat.getColor(context, id)
}

fun Double.toAlpha() : Int = (this * 255).toInt()