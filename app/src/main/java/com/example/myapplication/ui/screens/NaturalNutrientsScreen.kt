package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.network.FoodDetails
import com.example.myapplication.ui.NaturalViewModel

@Composable
fun NaturalNutrientsScreen(
    viewModel: NaturalViewModel,
    onBackClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    val foods by viewModel.foods.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onBackClick) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Let the user type "1 egg", "2 apples", etc.
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Query (e.g. '1 egg')") },
            modifier = Modifier.fillMaxWidth()
        )

        // Tap to fetch detailed data
        Button(
            onClick = {
                if (query.isNotBlank()) {
                    viewModel.fetchNutrients(query)
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Get Detailed Info")
        }

        // Show error if any
        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display each food item and its calories
        LazyColumn {
            items(foods) { item: FoodDetails ->
                Text(
                    text = "${item.food_name ?: "Unknown"} - ${item.nf_calories ?: 0.0} kcal",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
