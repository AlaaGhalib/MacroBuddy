package com.example.myapplication.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class CaloriesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val repository = (applicationContext as MyApplication).repository

            // Get start and end timestamps for the day
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfDay = cal.timeInMillis
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val endOfDay = cal.timeInMillis - 1

            // Fetch calories consumed for the day
            val calories = repository.getDailyCaloriesSum(startOfDay, endOfDay) ?: 0f

            // Get the last recorded weight
            val lastWeight = repository.getLastRecordedWeight() ?: 0f

            // Insert daily progress into the repository
            repository.insertDailyProgress(
                progress = com.example.myapplication.data.local.DailyProgress(
                    date = System.currentTimeMillis(),
                    calories = calories,
                    weight = lastWeight
                )
            )

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
