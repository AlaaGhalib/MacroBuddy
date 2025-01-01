package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [ConsumedFood::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun consumedFoodDao(): ConsumedFoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Double-checked locking to ensure singleton instance
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "macrobuddy_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
