package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MyApplication
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.data.local.DailyProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NutritionRepository = (application as MyApplication).repository

    private val _dailyProgress = MutableStateFlow<List<DailyProgress>>(emptyList())
    val dailyProgress = _dailyProgress.asStateFlow()

    init {
        fetchDailyProgress()
    }

    fun fetchDailyProgress() {
        viewModelScope.launch {
            _dailyProgress.value = repository.getDailyProgress()
        }
    }

    fun addDailyProgress(calories: Float, weight: Float?) {
        viewModelScope.launch {
            val lastWeight = weight ?: repository.getLastRecordedWeight() ?: 0f
            val currentTimestamp = Date().time // Convert Date to Long timestampv
            val progress = DailyProgress(date = currentTimestamp, calories = calories, weight = lastWeight)
            repository.insertDailyProgress(progress)
            fetchDailyProgress()
        }
    }
}
