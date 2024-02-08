package com.idle.togeduck.util

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.UUID

import kotlinx.coroutines.flow.first


object LoginUtil {
    var guid: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null

     fun setAll(guid:String, accessToken:String, refreshToken:String) {
        this.guid = guid
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

     fun setTokens(accessToken: String, refreshToken: String){
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }
}
