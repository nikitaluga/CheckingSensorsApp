package com.checking_sensors_app.presentation.ui.accelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import com.checking_sensors_app.data.DataStoreManager
import com.checking_sensors_app.extensions.roundTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val context: Context
) : ViewModel(), SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _sensorReadings = MutableStateFlow(Triple(0f, 0f, 0f))
    private val _sensorReadingsEmit = MutableStateFlow(Triple(0f, 0f, 0f))
    val sensorReadings: StateFlow<Triple<Float, Float, Float>> = _sensorReadings
    val sensorReadingsEmit: StateFlow<Triple<Float, Float, Float>> = _sensorReadings

    private val _newPinch = MutableStateFlow<Float?>(null)
    private val newPinch: StateFlow<Float?> = _newPinch

    private val _emitPitch = MutableStateFlow<Float?>(null)
    val emitPitch: StateFlow<Float?> = _emitPitch

    val mainHandler = Handler(Looper.getMainLooper())

    val updateTask = object : Runnable {
        override fun run() {
            _sensorReadingsEmit.value = sensorReadings.value
            _emitPitch.value = newPinch.value
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        _sensorReadings.value = Triple(
            event.values[0],
            event.values[1],
            event.values[2]
        )

        _newPinch.value = event.values[2]
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun checkPresenceAccelerometer(): Boolean {
        return sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size > 0
    }

    fun registerListener() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        sensorManager.unregisterListener(this)
    }

    fun setOrdinateValue(
        ordinateNumber: Int,
        pitchMax: Float,
        pitchMin: Float,
        average: Float
    ) = when (ordinateNumber) {
        0 -> pitchMax
        1 -> (pitchMax + average) / 2
        2 -> (average)
        3 -> (pitchMin + average) / 2
        else -> pitchMin
    }.roundTo(context)

    companion object {
        val UPDATE_FREQUENCY_KEY = intPreferencesKey("update_frequency")
    }

}