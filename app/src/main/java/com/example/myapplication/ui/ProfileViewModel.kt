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
    fun updateDailyGoal(goal: Float) {
        viewModelScope.launch {
            _goalCalories.value = goal
            // TODO: Integrate persistent storage (Room/DataStore) here later
        }
    }
}
/*// ProfileViewModel.kt
package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MyApplication
import com.example.myapplication.data.NutritionRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NutritionRepository = (application as MyApplication).repository

    // LiveData for the user's calorie goal
    val goalCalories: LiveData<Float> = repository.dailyGoalFlow.asLiveData()

    /**
     * Updates the user's daily calorie goal.
     *
     * @param newGoal The new calorie goal to set.
     */
    fun updateDailyGoal(newGoal: Float) {
        viewModelScope.launch {
            repository.updateDailyGoal(newGoal)
        }
    }
}
*/