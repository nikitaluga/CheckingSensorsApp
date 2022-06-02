package com.checking_sensors_app.presentation.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TitleCard(sensorName: String, @DrawableRes id: Int, contentDescription: String? = null) {
    Row {
        Image(
            bitmap = ImageBitmap.imageResource(id = id),
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = sensorName,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}