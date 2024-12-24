package com.example.myapplication.ui

import androidx.lifecycle.*
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.data.local.ConsumedFood
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(private val repository: NutritionRepository) : ViewModel() {

    // Let’s assume user sets dailyGoal in some shared preference or default
    private val dailyGoal = 2000 // example

    // Expose LiveData for UI
    val todaysFoods: LiveData<List<ConsumedFood>>
    val todaysCalorieSum: LiveData<Double>

    init {
        val (startOfDay, endOfDay) = getTodayStartEndTimestamps()
        todaysFoods = repository.getConsumedFoodsForDay(startOfDay, endOfDay)
        todaysCalorieSum = repository.getDailyCaloriesSum(startOfDay, endOfDay)
    }

    fun getRemainingCalories(caloriesConsumed: Double?): Double {
        val consumed = caloriesConsumed ?: 0.0
        return dailyGoal - consumed
    }

    fun searchAndAddFood(query: String) {
        viewModelScope.launch {
            val response = repository.searchFoodItem(query)
            // For simplicity, just take the first common item (if present)
            val firstFood = response.common?.firstOrNull()
            if (firstFood != null) {
                // Suppose we have nf_calories
                val newFood = ConsumedFood(
                    foodName = firstFood.food_name,
                    calories = firstFood.nf_calories ?: 0.0,
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
        cal.add(Calendar.DAY_OF_YEAR, 1) // move to next day
        val end = cal.timeInMillis - 1
        return start to end
    }
}
