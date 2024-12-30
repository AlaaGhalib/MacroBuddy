package com.example.myapplication.data

import com.example.myapplication.network.NaturalNutrientsRequest
import com.example.myapplication.network.NaturalNutrientsResponse
import com.example.myapplication.network.NutritionixApi
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.data.local.ConsumedFood
import com.example.myapplication.data.local.ConsumedFoodDao

class NutritionRepository(                                                  
    private val consumedFoodDao: ConsumedFoodDao // from Room
) {
    private val api: NutritionixApi = RetrofitClient.api

    // Provide your appId and apiKey via constructor or as constants
    private val appId = "134ff997"
    private val apiKey = "bf15a02919c6888eab5ca2449875d621\t"

    suspend fun searchFoodItem(query: String) = api.searchFoods(
        query = query,
        appId = appId,
        apiKey = apiKey
    )

    // Room operations
    suspend fun insertConsumedFood(food: ConsumedFood) {
        consumedFoodDao.insert(food)
    }

    suspend fun getNutrientsForFood(foodName: String): NaturalNutrientsResponse {
        val request = NaturalNutrientsRequest(query = foodName)
        return api.getNutrients(
            appId = appId,
            apiKey = apiKey,
            request = request
        )
    }

    fun getConsumedFoodsForDay(startOfDay: Long, endOfDay: Long) =
        consumedFoodDao.getFoodsForDay(startOfDay, endOfDay)

    // Optionally compute daily sum of calories in the DAO
    fun getDailyCaloriesSum(startOfDay: Long, endOfDay: Long) =
        consumedFoodDao.getDailyCalorieSum(startOfDay, endOfDay)
}
