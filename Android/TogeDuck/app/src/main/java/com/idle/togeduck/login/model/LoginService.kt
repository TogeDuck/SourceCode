package com.idle.togeduck.login.model

import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : Response<LoginResponse>
}