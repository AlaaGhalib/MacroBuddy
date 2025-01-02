package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_progress")
data class DailyProgress(
    @PrimaryKey val date: Long, // Store as timestamp for the day
    val calories: Float,
    val weight: Float
)
