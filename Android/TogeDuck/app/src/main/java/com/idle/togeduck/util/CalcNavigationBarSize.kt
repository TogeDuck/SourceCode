package com.idle.togeduck.util

import android.annotation.SuppressLint
import android.content.Context
import com.idle.togeduck.util.DpPxUtil.pxToDp

object CalcNavigationBarSize {
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getNavigationBarHeightToPx(context: Context) : Int {
        var navigationBarHeight = 0
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")

        if (resourceId > 0) navigationBarHeight = context.resources.getDimensionPixelSize(resourceId)

        return navigationBarHeight
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getNavigationBarHeightToDp(context: Context) : Int {
        var navigationBarHeight = 0
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")

        if (resourceId > 0) navigationBarHeight = context.resources.getDimensionPixelSize(resourceId)

        return pxToDp(navigationBarHeight, context).toInt()
    }
}