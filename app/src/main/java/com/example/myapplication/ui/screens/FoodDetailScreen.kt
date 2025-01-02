package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.FoodDetailViewModel
import com.example.myapplication.ui.HomeViewModel

@Composable
fun FoodDetailScreen(
    foodName: String,
    onNavigateToHome: () -> Unit,
    homeViewModel: HomeViewModel,
    viewModel: FoodDetailViewModel,
    onBackClick: () -> Unit
) {
    val details by viewModel.foodDetails.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // We'll store the user’s grams input
    var gramsInput by remember { mutableStateOf("") }

    // On entering, fetch
    LaunchedEffect(foodName) {
        if (foodName.isNotBlank()) {
            viewModel.fetchFoodDetails(foodName)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onBackClick) {
            Text("Back")
        }
        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage")
        }

        // Suppose details has only one item or we handle the first
        val item = details.firstOrNull()
        if (item != null) {
            Text("Food: ${item.food_name ?: "Unknown"}")
            Text("Default Calories: ${item.nf_calories ?: 0} kcal")
            Text("Serving weight: ${item.serving_weight_grams ?: 0} g")

            Spacer(modifier = Modifier.height(8.dp))

            // Let user input how many grams they're eating
            TextField(
                value = gramsInput,
                onValueChange = { gramsInput = it },
                label = { Text("How many grams?") },
                modifier = Modifier.fillMaxWidth()
            )

            // Calculate total cals based on ratio
            val totalCals = remember(item, gramsInput) {
                val inputGrams = gramsInput.toDoubleOrNull() ?: 0.0
                // If the item says 1 serving is X grams => Y cals
                // then for the user input grams:
                // ratio = (inputGrams / serving_weight_grams) * nf_calories
                val baseCals = item.nf_calories ?: 0.0
                val baseGrams = item.serving_weight_grams ?: 0.0
                if (baseGrams > 0) {
                    baseCals * (inputGrams / baseGrams)
                } else {
                    0.0
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Total Calories for $gramsInput g = ${String.format("%.2f", totalCals)} kcal")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (item != null) {
                    val inputGrams = gramsInput.toDoubleOrNull() ?: 0.0
                    val totalCalories = viewModel.computeCaloriesForGramInput(item, inputGrams)
                    viewModel.addToDailyIntake(item.food_name ?: "Unknown", totalCalories)

                    // Notify HomeViewModel to refresh data
                    homeViewModel.refreshData()

                    // Redirect to SearchScreen
                    onNavigateToHome()
                }
            }) {
                Text("Add to Daily Intake")
            }


        }
    }
}
