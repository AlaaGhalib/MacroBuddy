package com.example.myapplication.network

data class NaturalNutrientsResponse(
    val foods: List<FoodDetails>?
)

data class FoodDetails(
    val nf_calories: Double? // Only parse calories
)
