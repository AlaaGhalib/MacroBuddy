package com.example.myfitnessapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.local.ConsumedFood

@Dao
interface ConsumedFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(consumedFood: ConsumedFood)

    @Query("SELECT * FROM consumed_foods WHERE timestamp BETWEEN :start AND :end")
    fun getFoodsForDay(start: Long, end: Long): LiveData<List<ConsumedFood>>

    @Query("SELECT SUM(calories) FROM consumed_foods WHERE timestamp BETWEEN :start AND :end")
    fun getDailyCalorieSum(start: Long, end: Long): LiveData<Double>
}
