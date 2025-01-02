// HomeViewModel.kt
package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MyApplication
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.data.local.ConsumedFood
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NutritionRepository = (application as MyApplication).repository

    private val dailyGoal = 2000f // Example daily goal

    private val todayTimestamps = getTodayStartEndTimestamps()
    private val startOfDay = todayTimestamps.first
    private val endOfDay = todayTimestamps.second

    val todaysFoods = MutableLiveData<List<ConsumedFood>>(emptyList())
    val todaysCalorieSum = MutableLiveData<Float>(0f)

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            val foods = repository.getConsumedFoodsForDay(startOfDay, endOfDay)
            val calorieSum = repository.getDailyCaloriesSum(startOfDay, endOfDay) ?: 0f
            todaysFoods.postValue(foods)
            todaysCalorieSum.postValue(calorieSum)
        }
    }

    fun getRemainingCalories(): Float {
        return dailyGoal - (todaysCalorieSum.value ?: 0f)
    }

    fun removeFood(food: ConsumedFood) {
        viewModelScope.launch {
            repository.deleteConsumedFood(food) // Deletes the food from the database
            refreshData() // Refresh the list and calorie sum after deletion
        }
    }

    private fun getTodayStartEndTimestamps(): Pair<Long, Long> {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 1)
        val end = cal.timeInMillis - 1
        return Pair(start, end)
    }
}

