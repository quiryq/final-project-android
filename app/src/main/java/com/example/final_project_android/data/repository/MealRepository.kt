package com.example.final_project_android.data.repository


import com.example.final_project_android.data.api.RetrofitInstance
import com.example.final_project_android.data.db.MealDatabase
import com.example.final_project_android.data.model.Meal

class MealRepository(
    private val db: MealDatabase
) {

    suspend fun searchMeals(searchQuery: String) =
        RetrofitInstance.api.searchMeals(searchQuery)


    suspend fun getRandomMeal() =
        RetrofitInstance.api.getRandomMeal()



    suspend fun upsert(meal: Meal) = db.getMealDao().upsert(meal)


    suspend fun delete(meal: Meal) = db.getMealDao().deleteMeal(meal)


    fun getSavedMeals() = db.getMealDao().getAllMeals()
}