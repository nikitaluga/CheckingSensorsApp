package com.checking_sensors_app.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.checking_sensors_app.presentation.ui.HomeScreen
import com.checking_sensors_app.presentation.ui.accelerometer.AccelerometerScreen

@Composable
fun AppContent() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.Accelerometer.route) {
            AccelerometerScreen(navController)
        }
    }
}