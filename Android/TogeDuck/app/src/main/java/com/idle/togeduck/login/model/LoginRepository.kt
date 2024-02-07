package com.idle.togeduck.login.model

import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

interface LoginRepository {
    suspend fun login(
        loginRequest: LoginRequest
    ): Response<LoginResponse>
}

class DefaultLoginRepository @Inject constructor(
    private val loginService: LoginService,
) : LoginRepository {
    override suspend fun login(
        loginRequest: LoginRequest
    ): Response<LoginResponse> {
        return loginService.login(loginRequest)
    }
}