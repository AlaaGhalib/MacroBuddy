package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.network.FoodDetails
import com.example.myapplication.network.FoodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: NutritionRepository
) : ViewModel() {
    // previously: a flow for instant search results, e.g. `searchFoods(query)`

    private val _detailedFoods = MutableStateFlow<List<FoodDetails>>(emptyList())
    val detailedFoods = _detailedFoods.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun getDetailedFood(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.getNutrientsForFood(query)
                _detailedFoods.value = response.foods.orEmpty()
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An error occurred"
            }
        }
    }
}


