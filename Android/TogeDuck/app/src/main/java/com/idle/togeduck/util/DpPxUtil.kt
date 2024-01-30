package com.idle.togeduck.util

import android.content.Context

object DpPxUtil {
    fun pxToDp(px: Int, context: Context): Float = px / context.resources.displayMetrics.density

    fun dpToPx(dp: Int, context: Context): Int = (dp * context.resources.displayMetrics.density + 0.5f).toInt()
}