package com.example.ccp.service

import com.example.ccp.model.BoardDTO
import com.example.ccp.model.Category
import com.example.ccp.model.IngrBoard
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


interface ApiService : UserService {
    @GET("/api/boards/list")
    fun getAllBoards(): Call<List<BoardDTO>>

    @GET
    fun getImage(@Url imageUrl: String): Call<ResponseBody>

    @GET("/api/boards/{num}")
    fun getBoardByNum(@Path("num") num: Int): Call<BoardDTO>

    @POST("/api/boards")
    fun insertBoard(@Body boardDTO: BoardDTO?): Call<Void?>?


    @POST("/api/join")
    override fun join(@Body user: User?): Call<UserResponse?>?


    @GET("/api/boards/search")
    fun searchBoards(@Query("title") title: String): Call<List<BoardDTO>>
    // 게시물 내 재료 목록을 가져오는 메서드 추가
    @GET("/api/boards/{num}/ingredients")
    fun getIngredientsForBoard(@Path("num") num: Int): Call<List<IngrBoard>>

    // 게시물 내 총 가격 데이터를 가져오기
    @GET("/api/boards/{num}/totalPrice")
    fun getTotalPrice(@Path("num") num: Int): Call<Int>
    @POST("/api/boards/{boardNum}/calculatePrice")
    fun updatePrice(@Path("boardNum") boardNum: Int, @Body requestBody: UpdatePriceRequest): Call<Int>
    @GET("api/categories")
    fun getAllCategories(): Call<List<Category>>

    // 특정 카테고리에 속하는 게시물들을 가져오는 메서드
    @GET("/api/category/{categoryId}/boards")
    fun getBoardsByCategory(@Path("categoryId") categoryId: Long): Call<List<BoardDTO>>
}



data class UpdatePriceRequest(
    val ingredientName: String,
    val isOwned: Boolean
)