// NutritionRepository.kt
package com.example.myapplication.data

import androidx.lifecycle.LiveData
import com.example.myapplication.data.local.ConsumedFood
import com.example.myapplication.data.local.ConsumedFoodDao
import com.example.myapplication.data.local.DailyProgress
import com.example.myapplication.data.local.ProgressDao
import com.example.myapplication.network.NaturalNutrientsResponse
import com.example.myapplication.network.NutritionixApi
import com.example.myapplication.network.NutritionixResponse
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NutritionRepository(
    private val consumedFoodDao: ConsumedFoodDao,
    private val progressDao: ProgressDao,
    private val api: NutritionixApi
) {
    // API credentials
    private val appId = "134ff997" // Replace with your actual App ID
    private val apiKey = "bf15a02919c6888eab5ca2449875d621" // Replace with your actual API Key

    // Suspended function to fetch daily progress
    suspend fun getDailyProgress(): List<DailyProgress> {
        return progressDao.getAllProgress()
    }

    suspend fun insertDailyProgress(progress: DailyProgress) {
        progressDao.insertProgress(progress)
    }

    suspend fun getLastRecordedWeight(): Float? {
        return progressDao.getLastRecordedWeight()
    }

    // Room operations
    suspend fun insertConsumedFood(food: ConsumedFood) {
        consumedFoodDao.insert(food)
    }

    suspend fun deleteConsumedFood(food: ConsumedFood) {
        consumedFoodDao.delete(food)
    }

    suspend fun getConsumedFoodsForDay(startOfDay: Long, endOfDay: Long): List<ConsumedFood> =
        consumedFoodDao.getFoodsForDay(startOfDay, endOfDay)

    suspend fun getDailyCaloriesSum(startOfDay: Long, endOfDay: Long): Float? =
        consumedFoodDao.getDailyCalorieSum(startOfDay, endOfDay)

    // Network operations
    suspend fun searchFoodItem(query: String): NutritionixResponse {
        return api.searchFoods(query, appId, apiKey)
    }

    suspend fun getNutrientsForFood(foodName: String): NaturalNutrientsResponse {
        val request = com.example.myapplication.network.NaturalNutrientsRequest(query = foodName)
        return api.getNutrients(appId, apiKey, request)
    }

    suspend fun getProductByBarcode(barcode: String): NaturalNutrientsResponse {
        return api.getProductByBarcode(
            appId = appId,
            apiKey = apiKey,
            upc = barcode
        )
    }

    suspend fun getNutrientsForBarcode(barcode: String): NaturalNutrientsResponse {
        return api.searchFoodsByBarcode(
            barcode = barcode,
            appId = appId,
            apiKey = apiKey
        )
    }
}

