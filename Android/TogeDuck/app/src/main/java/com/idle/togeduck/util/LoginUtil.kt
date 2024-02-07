package com.idle.togeduck.util

import java.util.UUID

object LoginUtil {
    fun makeGUID() : String {
        return UUID.randomUUID().toString()
    }
}