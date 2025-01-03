package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: DailyProgress)

    @Query("SELECT * FROM dailyprogress ORDER BY date DESC")
    suspend fun getAllProgress(): List<DailyProgress>

    @Query("SELECT * FROM dailyprogress WHERE date = :date LIMIT 1")
    suspend fun getProgressForDate(date: Long): DailyProgress?

    @Query("SELECT weight FROM dailyprogress ORDER BY date DESC LIMIT 1")
    suspend fun getLastRecordedWeight(): Float?
}