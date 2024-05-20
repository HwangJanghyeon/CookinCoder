package com.example.ccp.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPreferencesHelper {

    private const val PREFS_NAME = "userPrefs"
    private const val USER_ID_KEY = "userId"
    private const val USERNAME_KEY = "username"
    private const val TOKEN_KEY = "token"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveLoginInfo(context: Context, userId: Long, username: String, token: String) {
        val sharedPreferences = getPreferences(context)
        with(sharedPreferences.edit()) {
            putLong(USER_ID_KEY, userId) // 사용자 ID 저장
            putString(USERNAME_KEY, username)
            putString(TOKEN_KEY, token)
            apply()
        }
    }


    fun saveUsername(context: Context, username: String) {
        val sharedPreferences = getPreferences(context)
        with(sharedPreferences.edit()) {
            putString(USERNAME_KEY, username)
            apply()
        }
    }

    fun getUsername(context: Context): String? {
        val sharedPreferences = getPreferences(context)
        return sharedPreferences.getString(USERNAME_KEY, null)
    }

    fun saveUserId(context: Context, userId: Long) {
        Log.d("SharedPreferencesHelper", "Saving user ID: $userId")
        val sharedPreferences = getPreferences(context)
        // 사용자 ID가 유효한 경우에만 저장하도록 조건을 추가합니다.
        if (userId != 0L) {
            with(sharedPreferences.edit()) {
                putLong(USER_ID_KEY, userId)
                apply()
            }
        } else {
            Log.e("SharedPreferencesHelper", "Invalid user ID: $userId")
        }
    }


    fun getUserId(context: Context): Long {
        val sharedPreferences = getPreferences(context)
        val userId = sharedPreferences.getLong(USER_ID_KEY, -1)
        Log.d("SharedPreferencesHelper", "Retrieved user ID: $userId")
        return userId
    }
    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = getPreferences(context)

        val username = sharedPreferences.getString(USERNAME_KEY, null)
        return !username.isNullOrEmpty()
    }
    fun clearLoginInfo(context: Context) {
        val sharedPreferences = getPreferences(context)
        with(sharedPreferences.edit()) {
            remove(USER_ID_KEY)
            remove(USERNAME_KEY)
            remove(TOKEN_KEY)
            apply()
        }
    }
}
