package com.checking_sensors_app.presentation.ui.accelerometer

import android.content.Context
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    LaunchedEffect(key1 = Unit, block = {
        viewModel.registerListener()
        viewModel.sensorReadings.collectLatest {
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
            LineChart(viewModel, pitchMax.value, pitchMin.value, pinchDiff.value)
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
            }
        }
    }
}

@Composable
fun LineChart(
    viewModel: AccelerometerViewModel,
    pitchMax: Float,
    pitchMin: Float,
    pitchDiff: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row {
                Column(modifier = Modifier
                    .weight(10f)
                    .height(100.dp)) {
                    TableHint()
                    AbscissaValues()
                }
                Column(modifier = Modifier
                    .weight(1f)
                    .height(100.dp)) {
                    OrdinateValues(viewModel, pitchMax, pitchMin, pitchDiff)
                }
            }
        }
    }
}

@Composable
private fun TableHint() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(pxToDp(175))
            .padding(4.dp)
    ) {
        val drawWidth = size.width

        var y = 0f
        repeat(5) {
            drawLine(
                start = Offset(0f, y),
                end = Offset(drawWidth, y),
                color = Color(0x467C7C7C),
                strokeWidth = 1f
            )
            y += 40f
        }

        val xCoordinates = drawWidth / 6f
        repeat(7) {
            val x = xCoordinates * it.toFloat()
            drawLine(
                start = Offset(x, -10f),
                end = Offset(x, 170f),
                color = Color(0x467C7C7C),
                strokeWidth = 1f
            )
        }
    }
}

@Composable
private fun AbscissaValues() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp),
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
    viewModel: AccelerometerViewModel,
    pitchMax: Float,
    pitchMin: Float,
    pitchDiff: Float
) {
    repeat(5) {
        Text(
            modifier = Modifier.fillMaxWidth().height(20.dp),
            text = viewModel.setOrdinateValue(it, pitchMax, pitchMin, pitchDiff),
            fontSize = 6.sp,
            textAlign = TextAlign.Center
        )
    }
}