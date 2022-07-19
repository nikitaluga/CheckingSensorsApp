package com.checking_sensors_app.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.checking_sensors_app.Settings

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = Settings.UPDATE_FREQUENCY_FILE_NAME
)