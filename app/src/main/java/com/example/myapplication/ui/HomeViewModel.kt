// HomeViewModel.kt
package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MyApplication
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.data.local.ConsumedFood
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NutritionRepository = (application as MyApplication).repository

    private val dailyGoal = 2000f // Example daily goal

    // Initialize timestamps without destructuring
    private val todayTimestamps = getTodayStartEndTimestamps()
    private val startOfDay: Long = todayTimestamps.first
    private val endOfDay: Long = todayTimestamps.second

    // LiveData for today's consumed foods
    var todaysFoods: List<ConsumedFood> = emptyList()


    // LiveData for today's calorie sum
    var todaysCalorieSum: Float? = 0f

    fun setVar() {
        viewModelScope.launch {
            todaysFoods = repository.getConsumedFoodsForDay(startOfDay, endOfDay)
            todaysCalorieSum = repository.getDailyCaloriesSum(startOfDay, endOfDay)
        }
    }
    fun getRemainingCalories(caloriesConsumed: Float?): Float {
        val consumed = caloriesConsumed ?: 0f
        return dailyGoal - consumed
    }

    fun searchAndAddFood(query: String) {
        viewModelScope.launch {
            val response = repository.searchFoodItem(query)
            val firstFood = response.common?.firstOrNull()
            if (firstFood != null) {
                val newFood = ConsumedFood(
                    foodName = firstFood.food_name ?: "Unknown",
                    calories = firstFood.nf_calories?.toFloat() ?: 0f,
                    timestamp = System.currentTimeMillis()
                )
                repository.insertConsumedFood(newFood)
            }
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
