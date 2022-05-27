package com.checking_sensors_app.extensions

import android.content.res.Resources
import androidx.compose.ui.unit.dp

fun pxToDp(px: Int) = (px / Resources.getSystem().displayMetrics.density).dp