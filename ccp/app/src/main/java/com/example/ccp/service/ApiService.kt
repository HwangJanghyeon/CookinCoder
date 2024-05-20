package com.example.ccp.service

import com.example.ccp.model.BoardDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {
    @GET("/api/boards")
    fun getAllBoards(): Call<List<BoardDTO>>

    @GET
    fun getImage(@Url imageUrl: String): Call<ResponseBody>

    @GET("/api/boards/{num}")
    fun getBoardByNum(@Path("num") num: Int): Call<BoardDTO?>?

    @POST("/api/boards")
    fun insertBoard(@Body boardDTO: BoardDTO?): Call<Void?>?

    @POST("/api/boards/{num}/calculatePrice")
    fun updatePrice(@Path("num") boardNum: Int, @Body requestBody: Map<String?, Any?>?): Call<Int?>?
}
