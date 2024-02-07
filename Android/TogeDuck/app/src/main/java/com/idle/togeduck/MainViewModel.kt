package com.idle.togeduck

import android.util.Log
import androidx.lifecycle.ViewModel
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.event.model.toEvent
import com.idle.togeduck.login.model.LoginRepository
import com.idle.togeduck.login.model.LoginRequest
import com.idle.togeduck.util.LoginUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preference: PreferenceModule
) : ViewModel() {
    suspend fun login(socialType: String, code: String) {
        val responseResult = loginRepository.login(LoginRequest(socialType, code))

        if(responseResult.isSuccessful){
            val body = responseResult.body()!!

            preference.setJwtAccessToken(body.data.accessToken)
            preference.setJwtRefreshToken(body.data.refreshToken)

            Log.d("로그", "MainViewModel - login() 호출됨 accessToken : ${preference.jwtAccessTokenFlow.first()}")
            Log.d("로그", "MainViewModel - login() 호출됨 refreshToken : ${preference.jwtRefreshTokenFlow.first()}")
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "응답 실패 ${errorBody}")
        }
    }
}