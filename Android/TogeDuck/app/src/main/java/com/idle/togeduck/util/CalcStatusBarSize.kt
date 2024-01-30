package com.idle.togeduck.util

import android.annotation.SuppressLint
import android.content.Context

object CalcStatusBarSize {
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getStatusBarHeightToDp(context: Context) : Int {
        var statusBarHeight = 0
        val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resourceId > 0) statusBarHeight = context.resources.getDimensionPixelSize(resourceId)

        return DpPxUtil.pxToDp(statusBarHeight, context).toInt()
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getStatusBarHeightToPx(context: Context) : Int {
        var statusBarHeight = 0
        val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resourceId > 0) statusBarHeight = context.resources.getDimensionPixelSize(resourceId)

        return statusBarHeight
    }
}