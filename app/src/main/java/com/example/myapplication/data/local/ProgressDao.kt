package com.example.myapplication.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyProgress(progress: DailyProgress)

    @Query("SELECT * FROM daily_progress ORDER BY date ASC")
    fun getAllDailyProgress(): LiveData<List<DailyProgress>>

    @Query("SELECT weight FROM daily_progress ORDER BY date DESC LIMIT 1")
    suspend fun getLastRecordedWeight(): Float?
}
