package com.checking_sensors_app.presentation.ui.accelerometer

import android.content.Context
import android.graphics.PointF
import android.util.Log
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.checking_sensors_app.HEIGHT_TABLE_PX
import com.checking_sensors_app.extensions.pxToDp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AccelerometerScreen(
    context: Context = LocalContext.current
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val viewModel = AccelerometerViewModel(context)

    val azimuth = remember { mutableStateOf(0f) }
    val pitch = remember { mutableStateOf(0f) }
    val roll = remember { mutableStateOf(0f) }

    val pitchMax = remember { mutableStateOf(0f) }
    val pitchMin = remember { mutableStateOf(0f) }
    val pinchDiff = remember { mutableStateOf(0f) }

    val pinchCoordinatesY = remember { mutableStateListOf(0f) }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.registerListener()
        viewModel.sensorReadingsEmit.collectLatest {
            azimuth.value = it.first
            pitch.value = it.second
            roll.value = it.third

            if (pitchMax.value < it.second) {
                pitchMax.value = it.second
                pinchDiff.value = pitchMax.value - pitchMin.value
            }
            if (pitchMin.value > it.second) {
                pitchMin.value = it.second
                pinchDiff.value = pitchMax.value - pitchMin.value
            }
        }
    })

    LaunchedEffect(key1 = Unit, block = {
        viewModel.updateTask.run()
        viewModel.emitPitch.collectLatest { value ->
            pinchCoordinatesY.add(value)
        }
    })

    DisposableEffect(dispatcher) {
        onDispose { viewModel.unregisterListener() }
    }

    Surface(color = MaterialTheme.colors.background) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Accelerometer",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                )
            }
        ) {
            LineChart(
                viewModel,
                pitchMax.value,
                pitchMin.value,
                pinchDiff.value / 2,
                pinchCoordinatesY
            )
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Azimut: ${azimuth.value}")
                Text("Pitch: ${pitch.value}")
                Text("Roll: ${roll.value}")
                Text("pinchY: ${pinchCoordinatesY.last()}")
            }
        }
    }
}

@Composable
fun LineChart(
    viewModel: AccelerometerViewModel,
    pitchMax: Float,
    pitchMin: Float,
    average: Float,
    pointCoordinates: List<Float>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp),
        elevation = 10.dp
    ) {
        val heightTableDp = pxToDp(HEIGHT_TABLE_PX)
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(8.dp)
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
}

@Composable
private fun TableHint(modifier: Modifier, pointCoordinates: List<Float>) {
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

        var distance = widthTable / 30
        val maxValue = pointCoordinates.maxOrNull() ?: 0f
        val points = mutableListOf<PointF>()

        pointCoordinates.forEachIndexed { index, data ->
            val indx: Float = index.toFloat()
            val y0 = if (data >= 0) (maxValue - data) * (HEIGHT_TABLE_PX / maxValue) else HEIGHT_TABLE_PX

            val x0 = 0f + widthTable * indx/30
            points.add(PointF(x0, y0))
        }

        Log.d("TAG", "TableHint: points = ${pointCoordinates.last()}")
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

@Composable
private fun AbscissaValues(modifier: Modifier) {
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

@Composable
private fun OrdinateValues(
    modifier: Modifier,
    heightTable: Dp,
    viewModel: AccelerometerViewModel,
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