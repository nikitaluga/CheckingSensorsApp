package com.checking_sensors_app.presentation.widgets

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.checking_sensors_app.HEIGHT_TABLE_PX
import com.checking_sensors_app.extensions.pxToDp

@Composable
fun TableHint(modifier: Modifier, pointCoordinates: List<Float>) {
    Canvas(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(pxToDp(HEIGHT_TABLE_PX))
            .padding(4.dp)
    ) {
        val widthTable = size.width
        val gapBetweenTwoLines = widthTable / 6

        var y = 0f
        repeat(5) {
            drawLine(
                start = Offset(0f, y),
                end = Offset(widthTable, y),
                color = Color(0x467C7C7C),
                strokeWidth = 1f
            )
            y += 40f
        }

        repeat(7) {
            val x = gapBetweenTwoLines * it.toFloat()
            drawLine(
                start = Offset(x, -10f),
                end = Offset(x, 170f),
                color = Color(0x467C7C7C),
                strokeWidth = 1f
            )
        }

        val distance = widthTable / 30
        val maxValue = pointCoordinates.maxOrNull() ?: 0f
        val minValue = pointCoordinates.minOrNull() ?: 0f
        val points = mutableListOf<PointF>()

        pointCoordinates.forEachIndexed { index, data ->
            val y0 =
                if (data >= 0) (maxValue - data) * (HEIGHT_TABLE_PX / maxValue)
                else HEIGHT_TABLE_PX - (minValue - data) * (HEIGHT_TABLE_PX / minValue)

            val x0 = widthTable - (distance * (pointCoordinates.size - index - 1))

            if (x0 >= 0) points.add(PointF(x0, y0))
        }

        for (i in 0 until points.size - 1) {
            drawLine(
                start = Offset(points[i].x, points[i].y),
                end = Offset(points[i + 1].x, points[i + 1].y),
                color = Color(0xFF3F51B5),
                strokeWidth = 1f
            )
        }
    }
}