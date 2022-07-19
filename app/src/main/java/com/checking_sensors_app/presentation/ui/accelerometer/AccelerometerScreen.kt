package com.checking_sensors_app.presentation.ui.accelerometer

import android.content.Context
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.checking_sensors_app.R
import com.checking_sensors_app.presentation.widgets.LineChart
import com.checking_sensors_app.presentation.widgets.TextWithValue
import com.checking_sensors_app.presentation.widgets.TitleCard
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SensorsScreen(
    context: Context = LocalContext.current
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val viewModel = SensorViewModel(context)

    val azimuth = remember { mutableStateOf(0f) }
    val pitch = remember { mutableStateOf(0f) }
    val roll = remember { mutableStateOf(0f) }

    val pitchMax = remember { mutableStateOf(0f) }
    val pitchMin = remember { mutableStateOf(0f) }
    val pinchDiff = remember { mutableStateOf(0f) }

    val pinchCoordinatesY = remember { mutableStateListOf<Float>() }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.registerListener()
        viewModel.sensorReadingsEmit.collectLatest {
            azimuth.value = it.first
            pitch.value = it.second
            roll.value = it.third
        }
    })

    LaunchedEffect(key1 = Unit, block = {
        viewModel.updateTask.run()
        viewModel.emitPitch.collectLatest { value ->
            if (value == null) return@collectLatest
            pinchCoordinatesY.add(value)
            if (pitchMax.value < value) {
                pitchMax.value = value
                pinchDiff.value = pitchMax.value + pitchMin.value
            }
            if (pitchMin.value > value) {
                pitchMin.value = value
                pinchDiff.value = pitchMax.value + pitchMin.value
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 10.dp
            ) {

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentHeight()
                ) {

                    TitleCard(sensorName = "Accelerometer", R.drawable.ic_accelerometer)

                    Spacer(modifier = Modifier.height(10.dp))

                    TextWithValue(
                        text = "azimuth",
                        value = azimuth.value,
                        unitOfMeasurement = "m/s²"
                    )
                    TextWithValue(
                        text = "pitch",
                        value = pitch.value,
                        unitOfMeasurement = "m/s²"
                    )
                    TextWithValue(
                        text = "roll",
                        value = roll.value,
                        unitOfMeasurement = "m/s²"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LineChart(
                        viewModel,
                        pitchMax.value,
                        pitchMin.value,
                        pinchDiff.value / 2,
                        pinchCoordinatesY
                    )
                }
            }
        }
    }
}