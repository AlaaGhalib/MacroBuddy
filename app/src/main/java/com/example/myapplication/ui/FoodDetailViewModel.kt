// FoodDetailViewModel.kt
package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MyApplication
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.data.local.ConsumedFood
import com.example.myapplication.network.FoodDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoodDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NutritionRepository = (application as MyApplication).repository

    // Holds the detailed FoodDetails from the API
    private val _foodDetails = MutableStateFlow<List<FoodDetails>>(emptyList())
    val foodDetails = _foodDetails.asStateFlow()

    // Error or status messages
    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    /**
     * Fetch food details based on the food name.
     */
    fun fetchFoodDetails(foodNameOrBarcode: String) {
        viewModelScope.launch {
            try {
                val response = if (foodNameOrBarcode.matches(Regex("\\d+"))) {
                    repository.getProductByBarcode(foodNameOrBarcode)
                } else {
                    repository.getNutrientsForFood(foodNameOrBarcode)
                }
                _foodDetails.value = response.foods.orEmpty()
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Error fetching details"
            }
        }
    }


    /**
     * Compute calories based on user input grams.
     */
    fun computeCaloriesForGramInput(details: FoodDetails, userGrams: Double): Double {
        val baseCals = details.nf_calories ?: 0.0
        val baseGrams = details.serving_weight_grams ?: 0.0
        return if (baseGrams > 0.0 && userGrams > 0.0) {
            baseCals * (userGrams / baseGrams)
        } else {
            0.0
        }
    }

    /**
     * Add consumed food to daily intake.
     */
    fun addToDailyIntake(foodName: String, totalCalories: Double) {
        viewModelScope.launch {
            repository.insertConsumedFood(
                ConsumedFood(
                    foodName = foodName,
                    calories = totalCalories.toFloat(),
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
    fun fetchFoodDetailsByBarcode(barcode: String) {
        viewModelScope.launch {
            try {
                val response = repository.getNutrientsForBarcode(barcode)
                _foodDetails.value = response.foods.orEmpty()
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Error fetching details"
            }
        }
    }

}
