package com.example.ccp.model




data class User(
    var username: String,
    var name: String?,
    var password: String?,
    var email: String?,
    var role: String? = null
)

data class UserResponse(
    var status: String,
    var message: String
) {
    fun isSuccess(): Boolean {
        return status == "success"
    }
}

data class LoginRequest(
    var username: String,
    var password: String
)




