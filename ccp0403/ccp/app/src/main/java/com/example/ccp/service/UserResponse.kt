package com.example.ccp.service

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val message: String,
    val success: Boolean
)
data class LoginResponse(
    @SerializedName("username") val username: String,
    @SerializedName("token") val token: String,
    @SerializedName("message") val message: String,
    @SerializedName("userId") val userId: Long
)

