package com.example.final_project_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.final_project_android.data.repository.MealRepository

class MealViewModelProviderFactory(
    private val mealRepository: MealRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(mealRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}