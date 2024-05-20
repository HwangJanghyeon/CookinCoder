package com.example.ccp.service

import com.example.ccp.model.CommentDTO
import com.example.ccp.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {
    // 댓글창 출력
    @GET("/api/comments")
    fun getAllComments(@Query("boardNum") boardNum: Int) : Call<List<CommentDTO>>

    // 댓글 작성
    @POST("/api/comments")
    fun addComments(@Body commentDTO: CommentDTO, @Query("boardNum") boardNum: Int, @Query("username") username:String): Call<Void>

    // 댓글 수정
    @PUT("/api/comments/{cnum}")
    fun updateComments(@Path("cnum") cnum: Int, @Body requestBody: CommentDTO): Call<CommentDTO>

    // 댓글 삭제
    @DELETE("/api/comments/{cnum}")
    fun deleteComments(@Path("cnum") cnum: Int): Call<Void>
}