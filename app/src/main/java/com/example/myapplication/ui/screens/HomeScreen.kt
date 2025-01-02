package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToProfile: () -> Unit, // New parameter
    viewModel: HomeViewModel
) {
    // Hardcoded calorie numbers for demonstration
    val consumed by viewModel.todaysCalorieSum.observeAsState(initial = 0f)
    val total = 2000f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        // 1) Show the circular indicator
        CalorieCircularIndicator(
            consumed = consumed,
            total = total
        )

        // 2) Button to navigate to Search
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToSearch) {
            Text("Go to Search")
        }

        // 3) Button to navigate to Profile
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onNavigateToProfile) {
            Text("Profile")
        }
    }
}


@Composable
fun CalorieCircularIndicator(
    consumed: Float,   // e.g. 1200
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


