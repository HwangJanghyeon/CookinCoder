package com.example.ccp.service

import com.example.ccp.model.BoardDTO
import com.example.ccp.model.Favorite
import com.example.ccp.model.MypageDTO
import com.example.ccp.model.PaymentRequest
import com.example.ccp.model.User
import retrofit2.Call
import retrofit2.http.*

interface MyPageService {

    @GET("/api/my/mycontent")
    fun myContent(@Query("userId") userId: Long): Call<List<BoardDTO>>

    @GET("/api/my/myHeartContent")
    fun myHeartContent(@Query("userId") userId: Long): Call<List<Favorite>>

    @GET("/api/my/myPayment")
    fun myPayment(@Query("userId") userId: Long): Call<List<PaymentRequest>>

    @GET("/api/my/getUserInfo")
    fun getUserInformation(@Query("userId") userId: Long): Call<User>



    // 전체 마이페이지 정보 조회
    @GET("/api/mypage/{userId}")
    fun getMypageInfo(@Path("userId") userId: Long): Call<MypageDTO>

    // 사용자의 username을 이용하여 사용자가 작성한 게시글 목록을 조회
    @GET("/api/mypage/posts/{username}")
    fun getUserPostsByUsername(@Path("username") username: String): Call<List<BoardDTO>>


}
