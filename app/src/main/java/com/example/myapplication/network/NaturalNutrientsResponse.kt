package com.example.myapplication.network

data class NaturalNutrientsResponse(
    val foods: List<FoodDetails>?
)

data class FoodDetails(
    val food_name: String?,
    val nf_calories: Double? // Detailed calorie info
    // Add more fields if needed (serving_qty, nf_protein, etc.)
)
