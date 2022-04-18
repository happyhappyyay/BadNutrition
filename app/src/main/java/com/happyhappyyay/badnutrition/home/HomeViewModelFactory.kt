package com.happyhappyyay.badnutrition.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.happyhappyyay.badnutrition.database.FoodDao
import com.happyhappyyay.badnutrition.database.NutritionDatabase

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory (
    private val application:Application,
    private val database: NutritionDatabase? = null,
    private val dao: FoodDao? = null
    )
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application,database!!) as T
        }
            throw IllegalArgumentException("ViewModel Not Found")
    }
}