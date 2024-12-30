package com.example.myapplication.network

data class NaturalNutrientsResponse(
    val foods: List<FoodDetails>?
)

data class FoodDetails(
    val food_name: String?,
    val nf_calories: Double?,
    val serving_weight_grams: Double?,  // for ratio-based approach
    // ...
)

