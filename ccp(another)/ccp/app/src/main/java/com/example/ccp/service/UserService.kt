package com.example.ccp.service

import com.example.ccp.model.LoginRequest
import com.example.ccp.model.LoginResponse
import com.example.ccp.model.User
import com.example.ccp.model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/api/join")
    fun join(@Body user: User?): Call<UserResponse?>?

    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest?): Call<LoginResponse?>?
}

