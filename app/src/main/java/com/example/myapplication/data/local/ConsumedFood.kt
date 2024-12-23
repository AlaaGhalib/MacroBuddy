package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumed_foods")
data class ConsumedFood(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodName: String,
    val calories: Double,
    val timestamp: Long // store as epoch time (System.currentTimeMillis())
)
