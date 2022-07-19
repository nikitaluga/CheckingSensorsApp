package com.checking_sensors_app.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.checking_sensors_app.presentation.ui.accelerometer.SensorViewModel

@Composable
fun OrdinateValues(
    modifier: Modifier,
    heightTable: Dp,
    viewModel: SensorViewModel,
    pitchMax: Float,
    pitchMin: Float,
    average: Float
) {
    Column(
        modifier = modifier
            .fillMaxWidth(0.1f)
            .height(heightTable + 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(5) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = viewModel.setOrdinateValue(it, pitchMax, pitchMin, average),
                fontSize = 6.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}