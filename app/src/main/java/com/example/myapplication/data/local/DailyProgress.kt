package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyProgress(
    val calories: Float,
    val weight: Float, // Ensure not nullable in schema
    val date: Long,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
