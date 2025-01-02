package com.example.myapplication.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
)

interface NutritionixApi {

    // 1) Instant Search (GET /v2/search/instant)
    @GET("v2/search/instant")
    suspend fun searchFoods(
        @Query("query") query: String,
        @Header("x-app-id") appId: String,
        @Header("x-app-key") apiKey: String
    ): NutritionixResponse

    // 2) Natural Nutrients (POST /v2/natural/nutrients)
    @POST("v2/natural/nutrients")
    suspend fun getNutrients(
        @Header("x-app-id") appId: String,
        @Header("x-app-key") apiKey: String,
        @Body request: NaturalNutrientsRequest
    ): NaturalNutrientsResponse

    @GET("v1_1/item")
    suspend fun getProductByBarcode(
        @Query("upc") upc: String,
        @Query("appId") appId: String,
        @Query("appKey") apiKey: String
    ): NaturalNutrientsResponse

    @GET("v1_1/item")
    suspend fun searchFoodsByBarcode(
        @Query("upc") barcode: String,
        @Query("appId") appId: String,
        @Query("appKey") apiKey: String
    ): NaturalNutrientsResponse

}

