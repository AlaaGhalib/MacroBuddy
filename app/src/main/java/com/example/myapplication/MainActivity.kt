// MainActivity.kt
package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.myapplication.ui.FoodDetailViewModel
import com.example.myapplication.ui.HomeViewModel
import com.example.myapplication.ui.ProfileViewModel
import com.example.myapplication.ui.SearchViewModel
import com.example.myapplication.ui.screens.BarcodeScannerScreen
import com.example.myapplication.ui.screens.FoodDetailScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.SearchScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.workers.CaloriesWorker
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupCalorieWorker()

        setContent {
            MyApplicationTheme {
                // Provide a top-level Scaffold
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                    // If you want a top bar or bottom bar,
                    // you can add it here as well.
                ) { innerPadding ->

                    // Create a NavController to manage navigation
                    val navController = rememberNavController()

                    // Set up NavHost with navigation routes
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Destination 1: "home"
                        composable("home") {
                            // Obtain HomeViewModel via viewModel()
                            val homeViewModel: HomeViewModel = viewModel()

                            HomeScreen(
                                onNavigateToSearch = {
                                    navController.navigate("search")
                                },
                                onNavigateToProfile = {
                                    navController.navigate("profile")
                                },
                                viewModel = homeViewModel
                            )
                        }

                        // Destination 2: "search"
                        composable("search") {
                            val searchViewModel: SearchViewModel = viewModel()

                            SearchScreen(
                                onBackClick = { navController.popBackStack() },
                                viewModel = searchViewModel,
                                onFoodSelected = { foodName ->
                                    navController.navigate("foodDetail/$foodName")
                                },
                                navController = navController // Pass the NavController here
                            )
                        }


                        // Destination 3: "foodDetail/{foodName}"
                        composable(
                            route = "foodDetail/{foodName}",
                            arguments = listOf(navArgument("foodName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val encodedFoodName = backStackEntry.arguments?.getString("foodName") ?: ""
                            val foodName = Uri.decode(encodedFoodName)

                            val detailViewModel: FoodDetailViewModel = viewModel()
                            val homeViewModel: HomeViewModel = viewModel()

                            FoodDetailScreen(
                                foodName = foodName,
                                viewModel = detailViewModel,
                                homeViewModel = homeViewModel,
                                onBackClick = { navController.popBackStack() },
                                onNavigateToHome = { navController.navigate("home") }
                            )
                        }


                        // Destination 4: "profile"
                        composable("profile") {
                            // Obtain ProfileViewModel via viewModel()
                            val profileViewModel: ProfileViewModel = viewModel()

                            ProfileScreen(
                                viewModel = profileViewModel,
                            )
                        }
                        composable("barcodeScanner") {
                            BarcodeScannerScreen(
                                onBarcodeDetected = { barcode ->
                                    navController.navigate("foodDetail/$barcode")
                                }
                            )
                        }

                    }
                }
            }
        }
    }
    private fun setupCalorieWorker() {
        val workRequest = PeriodicWorkRequestBuilder<CaloriesWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailyCalorieWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        // Observe worker results and update UI if needed
        WorkManager.getInstance(this)
            .getWorkInfosForUniqueWorkLiveData("DailyCalorieWorker")
            .observe(this) { workInfos ->
                val workInfo = workInfos?.firstOrNull()
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    lifecycleScope.launch {
                        // Notify ViewModel to refresh UI (e.g., update the graph)
                        val profileViewModel =
                            ViewModelProvider(this@MainActivity)[ProfileViewModel::class.java]
                        profileViewModel.fetchDailyProgress()
                    }
                }
            }
    }

    private fun calculateInitialDelay(): Long {
        val now = System.currentTimeMillis()
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis - now
    }
}

