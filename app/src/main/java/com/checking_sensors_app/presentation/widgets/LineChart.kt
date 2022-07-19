package com.checking_sensors_app.presentation.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.checking_sensors_app.Ui.HEIGHT_TABLE_PX
import com.checking_sensors_app.extensions.pxToDp
import com.checking_sensors_app.presentation.ui.accelerometer.SensorViewModel

@Composable
fun LineChart(
    viewModel: SensorViewModel,
    pitchMax: Float,
    pitchMin: Float,
    average: Float,
    pointCoordinates: List<Float>
) {
    val heightTableDp = pxToDp(HEIGHT_TABLE_PX)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (table, abscissa, ordinate) = createRefs()

        TableHint(Modifier.constrainAs(table) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(ordinate.start)
        }, pointCoordinates)
        AbscissaValues(Modifier.constrainAs(abscissa) {
            start.linkTo(parent.start)
            top.linkTo(table.bottom, margin = 8.dp)
            end.linkTo(ordinate.start)
        })
        OrdinateValues(Modifier.constrainAs(ordinate) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }, heightTableDp, viewModel, pitchMax, pitchMin, average)
    }
}