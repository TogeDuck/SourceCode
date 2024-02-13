package com.idle.togeduck

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.login.model.LoginRepository
import com.idle.togeduck.login.model.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preference: PreferenceModule,
) : ViewModel() {

    var guid: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null
    var userId: Long? = null

    var isRealTimeOn = false

    init {
        getFromLocalData()
    }

    fun getFromLocalData() {
        guid = runBlocking {
            preference.getGuid.first()
        }
        accessToken = runBlocking {
            preference.getAccessToken.first()
        }
        refreshToken = runBlocking {
            preference.getRefreshToken.first()
        }
    }

    suspend fun makeGUID(): String {
        val guidMade = UUID.randomUUID().toString()
        guid = guidMade
        Log.d("로그", "makeGuid 호출" + guidMade)
        runBlocking {
            preference.setGuid(guidMade)
        }
        delay(1000)
        return guidMade
    }

    suspend fun login(socialType: String) {
        if (guid != null) {
            val responseResult =
                loginRepository.login(LoginRequest(socialType, guid!!))

            if (responseResult.isSuccessful) {
                val body = responseResult.body()!!
                preference.setAccessToken(body.data.accessToken)
                preference.setRefreshToken(body.data.refreshToken)
                accessToken = body.data.accessToken
                refreshToken = body.data.refreshToken
                userId = body.data.userId
                delay(1000)
                Log.d("유저 정보", body.toString())
                Log.d(
                    "로그",
                    "MainViewModel - login() 호출됨 accessToken : ${preference.getAccessToken.first()}"
                )
                Log.d(
                    "로그",
                    "MainViewModel - login() 호출됨 refreshToken : ${preference.getRefreshToken.first()}"
                )
            } else {
                val errorBody = Json.decodeFromString<DefaultResponse>(
                    responseResult.errorBody()?.string()!!
                )
                Log.d("로그", "응답 실패 ${errorBody}")
            }
        }
    }

    suspend fun login(socialType: String, code: String): Boolean {
        val responseResult = loginRepository.login(LoginRequest(socialType, code))

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            preference.setAccessToken(body.data.accessToken)
            preference.setRefreshToken(body.data.refreshToken)
            getFromLocalData()

            Log.d(
                "로그",
                "MainViewModel - login() 호출됨 accessToken : ${preference.getAccessToken.first()}"
            )
            Log.d(
                "로그",
                "MainViewModel - login() 호출됨 refreshToken : ${preference.getRefreshToken.first()}"
            )
            return true
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "응답 실패 ${errorBody}")
            return false
        }
    }
}