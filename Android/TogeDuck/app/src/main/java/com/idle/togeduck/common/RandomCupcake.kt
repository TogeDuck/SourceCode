package com.idle.togeduck.common

import com.idle.togeduck.R

object RandomCupcake {
    private val list = listOf(
        R.drawable.common_cupcake1,
        R.drawable.common_cupcake2,
        R.drawable.common_cupcake3,
        R.drawable.common_cupcake4,
        R.drawable.common_cupcake5,
        R.drawable.common_cupcake6,
        R.drawable.common_cupcake7,
        R.drawable.common_cupcake8,
        R.drawable.common_cupcake9,
    )

    fun getImage() = list.random()
}