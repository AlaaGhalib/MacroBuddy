package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

@Composable
fun FoodListScreen() {
    // Sample data for the food items
    var foodList by remember { mutableStateOf(listOf("Apple", "Banana", "Orange", "Pizza")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Foods Consumed Today",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(foodList) { food ->
                FoodItem(
                    food = food,
                    onRemoveClick = {
                        foodList = foodList.filter { it != food }
                    }
                )
            }
        }
    }
}

@Composable
fun FoodItem(
    food: String,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = food,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onRemoveClick) {
            Text("Remove")
        }
    }
}
