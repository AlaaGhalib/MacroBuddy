package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.data.local.ConsumedFood
import com.example.myapplication.ui.NaturalViewModel
import com.example.myapplication.ui.SearchViewModel
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.NaturalNutrientsScreen
import com.example.myapplication.ui.screens.SearchScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myfitnessapp.data.local.ConsumedFoodDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // 1) Provide a top-level Scaffold
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                    // If you want a top bar or bottom bar,
                    // you can add it here as well.
                ) { innerPadding ->

                    // 2) Create a NavController to manage navigation
                    val navController = rememberNavController()

                    // 3) Wrap the NavHost in Modifier.padding(innerPadding)
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Destination 1: "home"
                        composable("home") {
                            HomeScreen(
                                onNavigateToSearch = {
                                    navController.navigate("search")
                                }
                            )
                        }

                        // Destination 2: "search"
                        composable("search") {
                            // 1) Create a stub (fake) DAO that does nothing:
                            val stubDao = object : ConsumedFoodDao {
                                override suspend fun insert(food: ConsumedFood) {
                                    // do nothing
                                }
                                override fun getFoodsForDay(startOfDay: Long, endOfDay: Long): LiveData<List<ConsumedFood>> {
                                    // return empty or placeholder data
                                    return MutableLiveData(emptyList())
                                }
                                override fun getDailyCalorieSum(startOfDay: Long, endOfDay: Long): LiveData<Double> {
                                    return MutableLiveData(0.0)
                                }
                            }

                            // 2) Pass this stubDao to NutritionRepository
                            val repository = remember {
                                NutritionRepository(stubDao)
                            }

                            // 3) Create the SearchViewModel
                            val searchViewModel = remember {
                                SearchViewModel(repository)
                            }

                            // 4) Show the screen
                            SearchScreen(
                                onBackClick = { navController.popBackStack() },
                                viewModel = searchViewModel
                            )
                        }
                        composable("natural") {
                            // Suppose we have a stub or real DAO; pass it to your repository
                            val stubDao = object : ConsumedFoodDao {
                                override suspend fun insert(food: ConsumedFood) {
                                    // do nothing
                                }
                                override fun getFoodsForDay(startOfDay: Long, endOfDay: Long): LiveData<List<ConsumedFood>> {
                                    // return empty or placeholder data
                                    return MutableLiveData(emptyList())
                                }
                                override fun getDailyCalorieSum(startOfDay: Long, endOfDay: Long): LiveData<Double> {
                                    return MutableLiveData(0.0)
                                }
                            }
                            val repository = remember {
                                NutritionRepository(stubDao)
                            }
                            val viewModel = remember {
                                NaturalViewModel(repository)
                            }

                            NaturalNutrientsScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }


                    }
                }
            }
        }
    }
}
