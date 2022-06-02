package com.checking_sensors_app.presentation.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun TextWithValue(text: String, value: Float, unitOfMeasurement: String) {
    Text(text = "$text = $value $unitOfMeasurement", fontSize = 8.sp)
}