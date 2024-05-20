package com.example.ccp.util

import com.example.ccp.service.ApiService
import com.example.ccp.service.CommentService
import com.example.ccp.service.IngrService
import com.example.ccp.service.MyPageService
import com.example.ccp.service.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.100.103.73:8005/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val userServiceRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient.newBuilder().addInterceptor(loggingInterceptor).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val ingrService: IngrService by lazy {
        retrofit.create(IngrService::class.java)
    }

    val userService: UserService by lazy {
        userServiceRetrofit.create(UserService::class.java)
    }
    val myPageService: MyPageService by lazy {
        retrofit.create(MyPageService::class.java)
    }
    val commentService : CommentService by lazy{
        retrofit.create(CommentService::class.java)
    }
}

