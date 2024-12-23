package com.example.myapplication.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// Replace these with the actual JSON structure from Nutritionix
data class NutritionixResponse(
    val common: List<FoodItem>?,
    val branded: List<FoodItem>?
)

data class FoodItem(
    val food_name: String,
    val serving_qty: Double,
    val serving_unit: String,
    val nf_calories: Double?
    // ... other fields you need
)

interface NutritionixApi {
    @GET("v2/search/instant")
    suspend fun searchFoods(
        @Query("query") query: String,
        @Header("x-app-id") appId: String,
        @Header("x-app-key") apiKey: String
    ): NutritionixResponse
}
