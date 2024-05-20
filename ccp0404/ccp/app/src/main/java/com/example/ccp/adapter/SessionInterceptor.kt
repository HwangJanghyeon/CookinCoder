package com.example.ccp.adapter

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class SessionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val sessionId = sharedPreferences.getString("JSESSIONID", "")

        val newRequest = if (sessionId?.isNotEmpty() == true) {
            // 세션 쿠키가 있는 경우 요청 헤더에 추가
            request.newBuilder()
                .addHeader("Cookie", "JSESSIONID=$sessionId")
                .build()
        } else {
            request
        }

        return chain.proceed(newRequest)
    }
}

fun addSessionInterceptor(okHttpClientBuilder: OkHttpClient.Builder, context: Context): OkHttpClient.Builder {
    val sessionInterceptor = SessionInterceptor(context)
    return okHttpClientBuilder.addInterceptor(sessionInterceptor)
}
