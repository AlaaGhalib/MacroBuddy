package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.local.ConsumedFood
import com.example.myapplication.ui.HomeViewModel
import androidx.compose.foundation.lazy.items

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel
) {
    val consumed by viewModel.todaysCalorieSum.observeAsState(initial = 0f)
    val total = 2000f
    val todaysFoods by viewModel.todaysFoods.observeAsState(emptyList())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // 1) Calorie Circular Indicator
        CalorieCircularIndicator(
            consumed = consumed,
            total = total
        )

        // 2) Navigation Buttons
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToSearch) { Text("Go to Search") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onNavigateToProfile) { Text("Profile") }

        Spacer(modifier = Modifier.height(16.dp))

        // 3) Food List
        Box(modifier = Modifier.fillMaxSize()) {
            if (todaysFoods.isEmpty()) {
                Text(
                    text = "No foods logged for today.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(todaysFoods) { food ->
                        FoodListItem(food = food) {
                            viewModel.removeFood(food)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodListItem(food: ConsumedFood, onRemoveClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = food.foodName)
        Button(onClick = onRemoveClick) {
            Text("Remove")
        }
    }
}


@Composable
fun CalorieCircularIndicator(
    consumed: Float,
    total: Float,      // e.g. 2000
    modifier: Modifier = Modifier
) {
    // Calculate progress: 0.0f to 1.0f
    val progress = (consumed / total).coerceIn(0f, 1f)

    // Basic layout
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Compose's built-in circular progress
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 8.dp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Show numeric details
        Text(text = "Consumed: ${consumed.toInt()} / ${total.toInt()} calories")
        Text(text = "Remaining: ${(total - consumed).coerceAtLeast(0f).toInt()} calories")
    }
}


