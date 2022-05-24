package com.checking_sensors_app.main

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Accelerometer: Screen("accelerometer")
}