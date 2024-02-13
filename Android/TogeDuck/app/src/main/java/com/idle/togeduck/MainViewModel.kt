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

    private val _guid = MutableLiveData<String>()
    val guid: LiveData<String> get() = _guid

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> get() = _accessToken
    private val _refreshToken = MutableLiveData<String>()
    val refreshToken: LiveData<String> get() = _refreshToken

    var isRealTimeOn = false

    init {
        getFromLocalData()
    }

    fun getFromLocalData(){
        _guid.postValue(runBlocking {
            preference.getGuid.first()
        })
        _accessToken.postValue(runBlocking {
            preference.getAccessToken.first()
        })
        _refreshToken.postValue(runBlocking {
            preference.getRefreshToken.first()
        })
    }

     fun makeGUID(): String {
         val guidMade = UUID.randomUUID().toString()
         _guid.postValue(guidMade)
        runBlocking {
            preference.setGuid(guidMade)
        }
         return guidMade
    }

     fun isAccessTokenPresent(): Boolean{
        if(!_accessToken.value.isNullOrEmpty()){
            return true
        }
        return false
    }

    suspend fun login(socialType: String) {
        val responseResult = loginRepository.login(guid.value?.let { LoginRequest(socialType, it) }!!)

        if(responseResult.isSuccessful){
            val body = responseResult.body()!!
            preference.setAccessToken(body.data.accessToken)
            preference.setRefreshToken(body.data.refreshToken)
            Log.d("유저 정보", body.toString())
            Log.d("로그", "MainViewModel - login() 호출됨 accessToken : ${preference.getAccessToken.first()}")
            Log.d("로그", "MainViewModel - login() 호출됨 refreshToken : ${preference.getRefreshToken.first()}")
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            Log.d("로그", "응답 실패 ${errorBody}")
        }
    }

    suspend fun login(socialType: String, code: String):Boolean {
        val responseResult = loginRepository.login(LoginRequest(socialType, code))

        if(responseResult.isSuccessful){
            val body = responseResult.body()!!

            preference.setAccessToken(body.data.accessToken)
            preference.setRefreshToken(body.data.refreshToken)
            getFromLocalData()

            Log.d("로그", "MainViewModel - login() 호출됨 accessToken : ${preference.getAccessToken.first()}")
            Log.d("로그", "MainViewModel - login() 호출됨 refreshToken : ${preference.getRefreshToken.first()}")
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