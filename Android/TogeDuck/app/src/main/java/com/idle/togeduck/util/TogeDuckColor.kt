package com.idle.togeduck.util

import com.idle.togeduck.R

enum class TogeDuckColor(
    val color_main_100: Int,
    val color_main_200: Int,
    val color_main_300: Int,
    val color_main_400: Int
) {
    DEFAULT(R.color.bts_main_100, R.color.bts_main_200, R.color.bts_main_300, R.color.bts_main_400),
    BTS(R.color.bts_main_100, R.color.bts_main_200, R.color.bts_main_300, R.color.bts_main_400)
}