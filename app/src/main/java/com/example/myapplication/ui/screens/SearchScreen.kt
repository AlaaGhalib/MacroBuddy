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

    val detailedFoods by viewModel.detailedFoods.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onBackClick) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Let user type the free-form query (e.g. "1 egg")
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Query (e.g. '1 egg')") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (query.isNotBlank()) {
                    // Instead of calling the instant search, call getDetailedFood(query)
                    viewModel.getDetailedFood(query)
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Search Detailed")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage", color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Show each returned item’s name + nf_calories
        LazyColumn {
            items(detailedFoods) { food ->
                Text(
                    text = "${food.food_name ?: "Unknown"} - ${food.nf_calories ?: 0} kcal",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
