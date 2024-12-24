package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.myfitnessapp.data.local.ConsumedFoodDao

@Database(
    entities = [ConsumedFood::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun consumedFoodDao(): ConsumedFoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "myfitnessapp_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
