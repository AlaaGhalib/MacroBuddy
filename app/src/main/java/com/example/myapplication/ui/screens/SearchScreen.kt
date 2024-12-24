package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun SearchScreen(
    onBackClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    Column {
        Text(text = "Search Screen")
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Foods") }
        )
        Button(onClick = { /* Implement Nutritionix search call here */ }) {
            Text("Search")
        }
        Button(onClick = onBackClick) {
            Text("Back to Home")
        }
    }
}
