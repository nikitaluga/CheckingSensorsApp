package com.checking_sensors_app.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.checking_sensors_app.BuildConfig
import com.checking_sensors_app.main.Screen

@Composable
fun SettingsScreen(navController: NavController) {

    val settingsList = listOf(
        Setting(
            "Accelerator",
            "Seconds in square meter",
            Screen.Home
        ),
        Setting(
            "Update frequency",
            "1 s",
            Screen.Accelerometer
        )
    )

    val otherSettingsList = listOf(
        Setting("Rate app"),
        Setting("Share app"),
        Setting("Version ${BuildConfig.VERSION_NAME}")
    )

    Surface(color = MaterialTheme.colors.background) {
        Scaffold(
            topBar = {
                TopAppBar {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ArrowBack")
                    }
                    Spacer(Modifier.weight(1f, true))
                    Text(
                        text = "Settings",
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        color = Color.White

                    )
                    Spacer(Modifier.weight(1f, true))
                }
            }
        ) {
            LazyColumn {
                item {
                    HeaderSetting(title = "Units of measurement")
                }
                items(settingsList) { item: Setting ->
                    SettingItem(navController, item)
                    if (settingsList.last() != item)
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.LightGray
                        )
                }
                item {
                    HeaderSetting(title = "Other")
                }
                items(otherSettingsList) { item: Setting ->
                    SettingItem(navController, item)
                    if (otherSettingsList.last() != item)
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.LightGray
                        )
                }
            }
        }
    }
}

@Composable
fun HeaderSetting(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        fontSize = 24.sp
    )
}

@Composable
fun SettingItem(navController: NavController, item: Setting) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (item.screen != null)
                navController.navigate(item.screen.route)
        },
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.Black
        )
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column {
                Text(
                    text = item.title,
                    fontSize = 18.sp
                )
                if (item.subtitle != null)
                    Text(
                        text = item.subtitle,
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (item.screen != null)
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                )
        }
    }
}

data class Setting(
    val title: String,
    val subtitle: String? = null,
    val screen: Screen? = null
)