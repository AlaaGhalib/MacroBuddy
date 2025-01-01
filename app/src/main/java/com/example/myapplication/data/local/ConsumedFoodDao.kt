package com.example.myapplication.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ConsumedFoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: ConsumedFood)

    @Delete
    suspend fun delete(food: ConsumedFood)

    @Query("SELECT * FROM consumed_foods WHERE timestamp BETWEEN :startOfDay AND :endOfDay")
    suspend fun getFoodsForDay(startOfDay: Long, endOfDay: Long): List<ConsumedFood>

    @Query("SELECT SUM(calories) FROM consumed_foods WHERE timestamp BETWEEN :startOfDay AND :endOfDay")
    suspend fun getDailyCalorieSum(startOfDay: Long, endOfDay: Long): Float?
}
