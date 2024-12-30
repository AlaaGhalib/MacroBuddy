package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.data.NutritionRepository
import com.example.myapplication.ui.FoodDetailViewModel
import com.example.myapplication.ui.ProfileViewModel
import com.example.myapplication.ui.SearchViewModel
import com.example.myapplication.ui.screens.FoodDetailScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.SearchScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.data.local.ConsumedFood
import com.example.myapplication.data.local.ConsumedFoodDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

                    // Initialize NutritionRepository with a stub DAO
                    val repository = remember {
                        object : ConsumedFoodDao {
                            override suspend fun insert(food: ConsumedFood) {
                                // do nothing
                            }

                            override suspend fun delete(food: ConsumedFood) {
                                // do nothing
                            }

                            override fun getFoodsForDay(startOfDay: Long, endOfDay: Long): LiveData<List<ConsumedFood>> {
                                // return empty or placeholder data
                                return MutableLiveData(emptyList())
                            }

                            override fun getDailyCalorieSum(startOfDay: Long, endOfDay: Long): LiveData<Float?> {
                                return MutableLiveData(0f)
                            }
                        }.let { dao ->
                            NutritionRepository(dao)
                        }
                    }

                    // Set up NavHost with navigation routes
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
                                },
                                onNavigateToProfile = { // New callback
                                    navController.navigate("profile")
                                }
                            )
                        }

                        // Destination 2: "search"
                        composable("search") {
                            val searchViewModel = remember { SearchViewModel(repository) }

                            SearchScreen(
                                onBackClick = { navController.popBackStack() },
                                viewModel = searchViewModel,
                                onFoodSelected = { foodName ->
                                    navController.navigate("foodDetail/$foodName")
                                }
                            )
                        }


                        // Destination 3: "foodDetail/{foodName}"
                        composable(
                            route = "foodDetail/{foodName}",
                            arguments = listOf(navArgument("foodName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val encodedFoodName = backStackEntry.arguments?.getString("foodName") ?: ""
                            val foodName = Uri.decode(encodedFoodName)

                            // Instantiate FoodDetailViewModel using remember
                            val detailViewModel: FoodDetailViewModel = remember { FoodDetailViewModel(repository) }

                            // Fetch food details when the composable is launched
                            LaunchedEffect(key1 = foodName) {
                                detailViewModel.fetchFoodDetails(foodName)
                            }

                            FoodDetailScreen(
                                foodName = foodName,
                                viewModel = detailViewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // Destination 4: "profile"
                        composable("profile") {
                            // Instantiate ProfileViewModel
                            val profileViewModel: ProfileViewModel = viewModel()

                            // Show the ProfileScreen
                            ProfileScreen(
                                viewModel = profileViewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
