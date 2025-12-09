package com.example.final_project_android.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_android.data.model.Meal
import com.example.final_project_android.data.model.MealResponse
import com.example.final_project_android.data.repository.MealRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MealViewModel(
    private val mealRepository: MealRepository
) : ViewModel() {

    val searchMealsLiveData: MutableLiveData<Response<MealResponse>> = MutableLiveData()
    val randomMealLiveData: MutableLiveData<Response<MealResponse>> = MutableLiveData()


    val errorLiveData: MutableLiveData<String> = MutableLiveData()



    fun searchMeals(query: String) = viewModelScope.launch {try {

        val response = mealRepository.searchMeals(query)

        if (response.isSuccessful && response.body() != null) {
            searchMealsLiveData.postValue(response)
        } else {

            searchLocal(query)
        }

    } catch (t: Throwable) {

        searchLocal(query)
        errorLiveData.postValue("Нет интернета. Поиск по сохраненным рецептам.")
    }
    }


    private suspend fun searchLocal(query: String) {
        val localMeals = mealRepository.searchMealsLocal(query)

        if (localMeals.isNotEmpty()) {

            val mealResponse = MealResponse(localMeals)
            searchMealsLiveData.postValue(Response.success(mealResponse))
        } else {
            errorLiveData.postValue("Нет интернета и рецепт не найден в сохраненных.")
        }
    }


    fun getRandomMeal() = viewModelScope.launch {
        try {
            val response = mealRepository.getRandomMeal()
            randomMealLiveData.postValue(response)
        } catch (t: Throwable) {
            errorLiveData.postValue("Не удалось загрузить случайный рецепт")
        }
    }


    fun saveMeal(meal: Meal) = viewModelScope.launch {
        mealRepository.upsert(meal)
    }

    fun deleteMeal(meal: Meal) = viewModelScope.launch {
        mealRepository.delete(meal)
    }


}