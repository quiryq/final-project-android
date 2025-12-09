package com.example.final_project_android.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.final_project_android.data.model.Meal

@Dao
interface MealDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)




    @Query("SELECT * FROM meals_table WHERE name LIKE '%' || :searchQuery || '%'")
    suspend fun searchMeals(searchQuery: String): List<Meal>

}
