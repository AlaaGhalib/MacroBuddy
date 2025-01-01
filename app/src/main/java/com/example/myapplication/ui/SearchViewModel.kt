package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MyApplication
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.network.FoodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NutritionRepository = (application as MyApplication).repository

    // Holds the list of foods returned by the search
    private val _searchResults = MutableStateFlow<List<FoodItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    // Holds any error message if the network call fails or other issues occur
    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    /**
     * searchFoods(query: String)
     * - Initiates a network call via the repository to fetch food data from Nutritionix.
     * - Updates [searchResults] if successful, or [errorMessage] if something goes wrong.
     */
    fun searchFoods(query: String) {
        viewModelScope.launch {
            try {
                // Make the network request
                val response = repository.searchFoodItem(query)

                // Combine 'common' + 'branded' lists, handle null safely
                val combinedList = (response.common.orEmpty() + response.branded.orEmpty())

                // Update the results and clear any previous error
                _searchResults.value = combinedList
                _errorMessage.value = ""
            } catch (e: Exception) {
                // Catch any exceptions (e.g. no internet, invalid API key, etc.)
                _errorMessage.value = e.localizedMessage ?: "An unknown error occurred."
            }
        }
    }
}
