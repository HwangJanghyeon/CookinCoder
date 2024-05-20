package com.example.ccp.service

class ApiResponse(
    val isSuccess: Boolean ,
    val message: String
) {
    override fun toString(): String {
        return "ApiResponse(isSuccess=$isSuccess, message='$message')"
    }
}
