package com.checking_sensors_app.presentation.ui.accelerometer

import android.content.Context
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.checking_sensors_app.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AccelerometerScreen(
    navController: NavController,
    context: Context = LocalContext.current
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val viewModel = AccelerometerViewModel(context)

    val azimuth = remember { mutableStateOf(0F) }
    val pitch = remember { mutableStateOf(0F) }
    val roll = remember { mutableStateOf(0F) }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.registerListener()
        viewModel.sensorReadings.collectLatest {
            azimuth.value = it.first
            pitch.value = it.second
            roll.value = it.third
        }
    })

    DisposableEffect(dispatcher) {
        onDispose { viewModel.unregisterListener() }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Azimut: ${azimuth.value.roundTo(context)}")
            Text("Pitch: ${pitch.value.roundTo(context)}")
            Text("Roll: ${roll.value.roundTo(context)}")
        }
    }
}

private fun Float.roundTo(context: Context): String {
    return context.getString(R.string.value_format, this)
}