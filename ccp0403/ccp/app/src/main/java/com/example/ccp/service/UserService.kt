package com.example.ccp.service

import com.example.ccp.model.LoginRequest
import com.example.ccp.model.User
import com.example.ccp.model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @POST("/api/join")
    fun join(@Body user: User?): Call<UserResponse?>?

    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @GET("user/{username}/info")
    fun getUserInfo(@Path("username") username: String): Call<User>
}

// UserInfo 모델 정의
data class UserInfo(
    val userId: String,
    val name: String,
    val email: String

)
