package com.example.myapplication.data

import com.example.myapplication.data.local.ConsumedFood
import com.example.myapplication.network.NutritionixApi
import com.example.myapplication.network.RetrofitClient
import com.example.myfitnessapp.data.local.ConsumedFoodDao

class NutritionRepository(
    private val consumedFoodDao: ConsumedFoodDao // from Room
) {
    private val api: NutritionixApi = RetrofitClient.api

    // Provide your appId and apiKey via constructor or as constants
    private val appId = "YOUR_APP_ID"
    private val apiKey = "YOUR_API_KEY"

    suspend fun searchFoodItem(query: String) = api.searchFoods(
        query = query,
        appId = appId,
        apiKey = apiKey
    )

    // Room operations
    suspend fun insertConsumedFood(food: ConsumedFood) {
        consumedFoodDao.insert(food)
    }

    fun getConsumedFoodsForDay(startOfDay: Long, endOfDay: Long) =
        consumedFoodDao.getFoodsForDay(startOfDay, endOfDay)

    // Optionally compute daily sum of calories in the DAO
    fun getDailyCaloriesSum(startOfDay: Long, endOfDay: Long) =
        consumedFoodDao.getDailyCalorieSum(startOfDay, endOfDay)
}
