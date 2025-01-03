// MyApplication.kt
package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.network.RetrofitClient

class MyApplication : Application() {
    // Initialize the Room database
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    // Initialize the NutritionRepository
    val repository: NutritionRepository by lazy {
        NutritionRepository(
            consumedFoodDao = database.consumedFoodDao(),
            progressDao = database.progressDao(),
            api = RetrofitClient.api
        )
    }
}
