package com.checking_sensors_app.presentation.ui.accelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AccelerometerViewModel(
    context: Context
) : ViewModel(), SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _sensorReadings = MutableStateFlow(Triple(0f, 0f, 0f))
    val sensorReadings: StateFlow<Triple<Float, Float, Float>> = _sensorReadings

    override fun onSensorChanged(event: SensorEvent) {
        _sensorReadings.value = Triple(event.values[0], event.values[1], event.values[2])
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun registerListener() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        sensorManager.unregisterListener(this)
    }
}