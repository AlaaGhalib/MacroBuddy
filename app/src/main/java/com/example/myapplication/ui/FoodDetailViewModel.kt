package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.network.FoodDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoodDetailViewModel(
    private val repository: NutritionRepository
) : ViewModel() {

    // Holds the “official” FoodDetails object(s) from the natural endpoint
    private val _foodDetails = MutableStateFlow<List<FoodDetails>>(emptyList())
    val foodDetails = _foodDetails.asStateFlow()

    // Error or status messages
    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    /**
     * fetchFoodDetails(foodName):
     * Calls your repository to retrieve the detailed data for a single item,
     * populates [_foodDetails] for UI to observe.
     */
    fun fetchFoodDetails(foodName: String) {
        viewModelScope.launch {
            try {
                val response = repository.getNutrientsForFood(foodName)
                _foodDetails.value = response.foods.orEmpty()
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Error fetching details"
            }
        }
    }

    /**
     * computeCaloriesForGramInput(
     *   details: FoodDetails,
     *   userGrams: Double
     * ): Double
     *
     * Utility function that uses ratio-based calculation
     *   e.g.: if details.nf_calories is for “serving_weight_grams” grams,
     *         then for userGrams we do: ratio = baseCals * (userGrams / baseGrams).
     */
    fun computeCaloriesForGramInput(details: FoodDetails, userGrams: Double): Double {
        val baseCals = details.nf_calories ?: 0.0
        val baseGrams = details.serving_weight_grams ?: 0.0
        return if (baseGrams > 0.0 && userGrams > 0.0) {
            baseCals * (userGrams / baseGrams)
        } else 0.0
    }

    /**
     * addToDailyIntake(
     *   foodName: String,
     *   totalCalories: Double
     * )
     *
     * If you want to store the user’s consumption in your local DB,
     * call “repository.insertConsumedFood(...)” here, passing the needed info.
     */
    fun addToDailyIntake(foodName: String, totalCalories: Double) {
        viewModelScope.launch {
            // If you have an entity like “ConsumedFood”
            // or some method in your repository:
            repository.insertConsumedFood(
                // Adjust to your actual data class
                com.example.myapplication.data.local.ConsumedFood(
                    foodName = foodName,
                    calories = totalCalories,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
