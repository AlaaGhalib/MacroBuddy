package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.network.NaturalNutrientsResponse
import com.example.myapplication.network.FoodDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * A ViewModel that uses the "natural nutrients" endpoint
 * to get detailed calorie and nutrition info.
 */
class NaturalViewModel(
    private val repository: NutritionRepository
) : ViewModel() {

    // StateFlow that holds the list of returned foods
    private val _foods = MutableStateFlow<List<FoodDetails>>(emptyList())
    val foods = _foods.asStateFlow()

    // StateFlow for error handling
    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    /**
     * fetchNutrients(query):
     * Takes a string like "1 egg" or "2 apples"
     * and calls repository.getNutrientsForFood(...)
     * to get accurate nf_calories and other fields.
     */
    fun fetchNutrients(query: String) {
        viewModelScope.launch {
            try {
                // This calls getNutrientsForFood("1 egg") or similar
                val response: NaturalNutrientsResponse = repository.getNutrientsForFood(query)

                // The response may contain multiple foods in "foods"
                _foods.value = response.foods.orEmpty()
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An error occurred"
            }
        }
    }
}
