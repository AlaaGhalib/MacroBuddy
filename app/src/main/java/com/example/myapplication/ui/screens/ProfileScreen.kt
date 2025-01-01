package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    // State for user input in the TextField
    var goalInput by remember { mutableStateOf("") }

    // Collect the current goal from the ViewModel
    val currentGoal by viewModel.goalCalories.collectAsState()

    // SnackbarHostState for showing Snackbars
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Button
            Button(onClick = onBackClick) {
                Text("Back")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Set Your Daily Calorie Goal",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Field for Calorie Goal
            TextField(
                value = goalInput,
                onValueChange = { goalInput = it },
                label = { Text("Enter Calorie Goal (kcal)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            /*Button(
                onClick = {
                    val goal = goalInput.toFloatOrNull()
                    if (goal != null && goal > 0) {
                        viewModel.setGoalCalories(goal)
                        goalInput = ""
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Goal saved successfully!")
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please enter a valid number.")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Goal")
            }*/

            Spacer(modifier = Modifier.height(24.dp))

            // Display Current Goal
            Text(
                text = "Current Goal: ${currentGoal.toInt()} kcal",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
