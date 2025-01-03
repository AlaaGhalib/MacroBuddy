package com.example.myapplication.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.ProfileViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val dailyProgress by viewModel.dailyProgress.collectAsState(initial = emptyList())

    var calorieInput by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }

    // Format the dates from dailyProgress for x-axis labels
    val dateFormatter = SimpleDateFormat("MM/dd", Locale.getDefault())
    val labels = dailyProgress.map { dateFormatter.format(Date(it.date)) }.reversed()
    val calorieData = dailyProgress.map { it.calories }.reversed()
    val weightData = dailyProgress.map { it.weight ?: 0f }.reversed()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = calorieInput,
            onValueChange = { calorieInput = it },
            label = { Text("Enter today's calories") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            label = { Text("Enter today's weight (optional)") }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val calories = calorieInput.toFloatOrNull() ?: 0f
            val weight = weightInput.toFloatOrNull()
            viewModel.addDailyProgress(calories, weight)
            calorieInput = ""
            weightInput = ""
        }) {
            Text("Submit")
        }

        Text(text = "Daily Progress")
        if (dailyProgress.isNotEmpty()) {
            // Pass reversed data and labels to the chart
            CustomLineChart(
                data = calorieData,
                labels = labels
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomLineChart(
                data = weightData,
                labels = labels
            )
        } else {
            Text("No progress data available.")
        }
    }
}





@Composable
fun CustomLineChart(
    data: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty() || labels.size != data.size) {
        Text("No data available or labels mismatch")
        return
    }

    Canvas(modifier = modifier.height(300.dp).fillMaxWidth()) {
        val padding = 50f
        val chartWidth = size.width - padding
        val chartHeight = size.height - padding

        val maxValue = data.maxOrNull() ?: 1f
        val minValue = data.minOrNull() ?: 0f
        val range = maxValue - minValue
        val stepX = chartWidth / (data.size - 1)

        // Draw horizontal grid lines and y-axis labels
        val yStep = chartHeight / 5
        for (i in 0..5) {
            val yValue = minValue + (range / 5 * i)
            val yPos = chartHeight - (i * yStep)

            // Draw grid line
            drawLine(
                color = Color.LightGray,
                start = Offset(padding, yPos),
                end = Offset(size.width, yPos),
                strokeWidth = 1.dp.toPx()
            )

            // Draw y-axis labels
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.1f", yValue),
                10f,
                yPos + 10f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 24f
                }
            )
        }

        // Draw x-axis labels
        labels.forEachIndexed { index, label ->
            val xPos = padding + index * stepX

            drawContext.canvas.nativeCanvas.drawText(
                label,
                xPos - 20f,
                size.height - 10f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 24f
                }
            )
        }

        // Draw data points and connecting lines
        val points = data.mapIndexed { index, value ->
            Offset(
                padding + index * stepX,
                chartHeight - ((value - minValue) / range * chartHeight)
            )
        }

        // Draw connecting lines
        for (i in 1 until points.size) {
            drawLine(
                color = Color.White,
                start = points[i - 1],
                end = points[i],
                strokeWidth = 4.dp.toPx()
            )
        }

        // Draw data points
        points.forEach { point ->
            drawCircle(
                color = Color.Red,
                radius = 4.dp.toPx(),
                center = point
            )
        }
    }
}