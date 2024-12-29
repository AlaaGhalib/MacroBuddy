package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.network.FoodItem
import com.example.myapplication.ui.SearchViewModel

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    viewModel: SearchViewModel
) {
    var query by remember { mutableStateOf("") }

    val results by viewModel.searchResults.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onBackClick) {
            Text("Back to Home")
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Foods") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (query.isNotBlank()) {
                    viewModel.searchFoods(query)
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Search")
        }

        // Show any network error
        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn for multiple results
        LazyColumn {
            items(results) { item ->
                // For each FoodItem, display a clickable row
                FoodItemRow(
                    foodItem = item,
                    onItemClick = {
                        // You can call a detail function, or store it, etc.
                        // For example:
                        // viewModel.addConsumedFood(it)
                    }
                )
            }
        }
    }
}

@Composable
fun FoodItemRow(
    foodItem: FoodItem,
    onItemClick: (FoodItem) -> Unit
) {
    // Provide a fallback for missing calories
    val calories = foodItem.nf_calories ?: 0.0

    // Wrap everything in a clickable surface or row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(foodItem) }
            .padding(8.dp)
    ) {
        Text(
            text = "${foodItem.food_name} - ${calories} kcal",
            modifier = Modifier.weight(1f)
        )
    }
}


