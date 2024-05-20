package com.example.ccp.service

import com.example.ccp.model.LoginRequest
import com.example.ccp.model.User
import com.example.ccp.model.UserResponse
import com.google.gson.annotations.SerializedName
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


data class LoginResponse(
    @SerializedName("id") val userId: Long,
    @SerializedName("username") val username: String,
    @SerializedName("token") val token: String,
    @SerializedName("message") val message: String

)
