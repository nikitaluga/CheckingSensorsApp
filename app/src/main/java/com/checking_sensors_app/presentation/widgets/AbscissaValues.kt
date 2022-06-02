package com.checking_sensors_app.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun AbscissaValues(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var timeStampMax = 30
        repeat(7) {
            Text(
                text = if (timeStampMax == 5 || timeStampMax == 0) "$timeStampMax  " else timeStampMax.toString(),
                fontSize = 6.sp,
                textAlign = TextAlign.Start
            )
            timeStampMax -= 5
        }
    }
}