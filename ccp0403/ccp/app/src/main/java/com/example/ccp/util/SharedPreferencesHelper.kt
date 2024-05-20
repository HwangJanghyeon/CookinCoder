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

        // 사용자 ID를 저장하는 함수
        fun saveUserId(context: Context, userId: Long) {
            Log.d("SharedPreferencesHelper", "Saving user ID: $userId")
            val sharedPreferences = getPreferences(context)
            with(sharedPreferences.edit()) {
                putLong(USER_ID_KEY, userId)
                apply()
            }
        }

        fun getUserId(context: Context): Long {
            val sharedPreferences = getPreferences(context)
            val userId = sharedPreferences.getLong(USER_ID_KEY, -1)
            Log.d("SharedPreferencesHelper", "Retrieved user ID: $userId")
            return userId
        }

    }
