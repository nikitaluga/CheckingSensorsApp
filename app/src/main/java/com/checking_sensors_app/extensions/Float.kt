package com.checking_sensors_app.extensions

import android.content.Context
import com.checking_sensors_app.R

fun Float.roundTo(context: Context): Float {
    return context.getString(R.string.value_format, this).toFloat()
}