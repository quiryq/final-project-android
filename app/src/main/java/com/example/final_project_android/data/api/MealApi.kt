package com.example.final_project_android.data.api


import com.example.final_project_android.data.model.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {


    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): Response<MealResponse>


    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealResponse>
}