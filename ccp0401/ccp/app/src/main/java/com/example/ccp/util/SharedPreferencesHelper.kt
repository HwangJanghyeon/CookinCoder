package com.example.ccp.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {

    private const val PREFS_NAME = "userPrefs"
    private const val USERNAME_KEY = "username"
    private const val TOKEN_KEY = "token"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveLoginInfo(context: Context, username: String, token: String) {
        val sharedPreferences = getPreferences(context)
        with(sharedPreferences.edit()) {
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
}
