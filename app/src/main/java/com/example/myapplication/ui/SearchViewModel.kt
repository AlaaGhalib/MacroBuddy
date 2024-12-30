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

    private val _searchResults = MutableStateFlow<List<FoodItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun searchFoods(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchFoodItem(query)
                // Combine common + branded
                _searchResults.value = (response.common.orEmpty() + response.branded.orEmpty())
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An error occurred"
            }
        }
    }
}



