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
import androidx.navigation.NavController
import com.example.myapplication.network.FoodItem
import com.example.myapplication.ui.SearchViewModel
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onFoodSelected: (String) -> Unit, // callback to navigate to detail
    viewModel: SearchViewModel,
    navController: NavController // Pass NavController here
) {
    var query by remember { mutableStateOf("") }
    val results by viewModel.searchResults.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onBackClick) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // TextField for the user to type the search query
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("barcodeScanner") }) {
            Text("Scan Barcode")
        }

        // Show error if any
        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display results
        LazyColumn {
            items(results) { item ->
                // We show partial calorie info from the instant endpoint
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Navigate to detail route with item.food_name
                            onFoodSelected(item.food_name)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text("${item.food_name}")
                }
            }
        }
    }
}

