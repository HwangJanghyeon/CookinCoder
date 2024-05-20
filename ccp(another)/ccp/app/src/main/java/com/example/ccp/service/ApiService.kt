package com.example.ccp.service

import com.example.ccp.model.IngrBoard
import com.example.ccp.model.BoardDTO
import com.example.ccp.model.LoginRequest
import com.example.ccp.model.LoginResponse
import com.example.ccp.model.User
import com.example.ccp.model.UserResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("/api/boards")
    fun getAllBoards(): Call<List<BoardDTO>>

    @GET
    fun getImage(@Url imageUrl: String): Call<ResponseBody>

    // 게시글 불러오기
    @GET("/api/boards/{num}")
    fun getBoardByNum(@Path("num") num: Int): Call<BoardDTO?>

    @POST("/api/boards")
    fun insertBoard(@Body boardDTO: BoardDTO?): Call<Void?>

    @POST("/api/boards/{num}/calculatePrice")
    fun updatePrice(@Path("num") boardNum: Int, @Body requestBody: Map<String?, Any?>?): Call<Int?>

    @GET("/api/boards/search")
    fun searchBoards(@Query("title") title: String): Call<List<BoardDTO>>

    @POST("/api/join")
    fun join(@Body user: User?): Call<UserResponse?>

    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest?): Call<LoginResponse?>

    // 게시물 내 재료 목록을 가져오는 메서드 추가
    @GET("/api/boards/{num}/ingredients")
    fun getIngredientsForBoard(@Path("num") num: Int): Call<List<IngrBoard>>

    // 게시물 내 총 가격 데이터를 가져오기
    @GET("/api/boards/{num}/totalPrice")
    fun getTotalPrice(@Path("num") num: Int): Call<Int>
}
