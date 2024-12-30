package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    // MutableStateFlow to hold the user's calorie goal
    private val _goalCalories = MutableStateFlow<Float>(2000f) // Default goal
    val goalCalories = _goalCalories.asStateFlow()

    // Function to update the calorie goal
    fun setGoalCalories(goal: Float) {
        viewModelScope.launch {
            _goalCalories.value = goal
            // TODO: Integrate persistent storage (Room/DataStore) here later
        }
    }
}
