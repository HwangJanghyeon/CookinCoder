package com.example.ccp.model




data class User(
    var id: Long,
    var username: String,
    var name: String?,
    var password: String?,
    var email: String?,
    var role : String?,

    )
data class UserResponse(
    var message: String
) {
    fun isSuccess(): Boolean {
        return true
    }
}

data class LoginRequest(
    var username: String,
    var password: String
)




