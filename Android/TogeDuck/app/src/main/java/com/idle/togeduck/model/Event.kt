package com.idle.togeduck.model

data class Event(
    val posterImgUrl: String,
    val cafeName: String,
    val eventName: String,
    val eventPeriod: String,
    var bookmarkBtn: Boolean
)
