package com.checking_sensors_app.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.checking_sensors_app.presentation.ui.HomeScreen
import com.checking_sensors_app.presentation.ui.SettingsScreen
import com.checking_sensors_app.presentation.ui.accelerometer.SensorsScreen

@Composable
fun AppContent() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Accelerometer.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.Accelerometer.route) {
            SensorsScreen(navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}