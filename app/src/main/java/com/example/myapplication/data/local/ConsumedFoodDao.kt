package com.example.myapplication.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.data.local.ConsumedFood

@Dao
interface ConsumedFoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: ConsumedFood)

    @Delete
    suspend fun delete(food: ConsumedFood)

    @Query("SELECT * FROM consumed_foods WHERE timestamp BETWEEN :startOfDay AND :endOfDay")
    fun getFoodsForDay(startOfDay: Long, endOfDay: Long): LiveData<List<ConsumedFood>>

    @Query("SELECT SUM(calories) FROM consumed_foods WHERE timestamp BETWEEN :startOfDay AND :endOfDay")
    fun getDailyCalorieSum(startOfDay: Long, endOfDay: Long): LiveData<Float?>
}
