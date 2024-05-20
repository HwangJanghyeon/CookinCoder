package com.example.ccp.service


import com.example.ccp.model.BoardDTO
import com.example.ccp.model.Category
import com.example.ccp.model.DataDTO
import com.example.ccp.model.IngrBoard
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface IngrService {

    @GET("/api/get_names")
    fun getNamesByCategory(@Query("categoryId") categoryId: String): Call<List<DataDTO>>
    @POST("/api/submit_all_forms")
    fun submitAllForms(@Body forms: List<IngrBoard>): Call<ApiResponse>

    @Multipart
    @POST("/api/submit_recipe")
    fun submitRecipe(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<String>


    @POST("/api/submit_all_forms-update")
    fun submitAllFormsUpdate(@Body ingredientForms: List<IngrBoard>): Call<Void>

    @PUT("/api/submit_recipe_update")
    fun submitRecipeUpdate(@Body recipeForm: BoardDTO): Call<Void>

    @DELETE("/api/delete/{num}")
    fun deleteRecipe(@Path("num") num: Int, @Body requestBody: Map<String, String>): Call<Int>

    @GET("/api/categories")
    fun getAllCategories(): Call<List<Category>>

    // 특정 카테고리에 속하는 게시물들을 가져오는 메서드
    @GET("/api/boards/category/{categoryId}")
    fun getBoardsByCategory(@Path("categoryId") categoryId: Long): Call<List<BoardDTO>>
}
