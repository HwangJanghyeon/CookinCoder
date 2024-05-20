package com.example.ccp.model

import android.graphics.Bitmap
import okhttp3.MultipartBody
import java.util.Date
import java.util.Locale


data class BoardDTO(
    val num: Int,
    val category: Category,
    val title: String,
    val writer: User,
    val content: String,
    val regdate: Date,
    val hitcount: Int,
    val replyCnt: Int?,
    val totalprice: Int,
    val imageUrl: String,
){

    val searchableTitle: String
        get() = title.toLowerCase(Locale.getDefault())
}

// MultipartFile 대신 사용할 클래스 (파일 업로드 시)
data class FileUploadDTO(
    val image: MultipartBody.Part
)
