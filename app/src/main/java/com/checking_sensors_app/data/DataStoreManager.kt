package com.checking_sensors_app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.checking_sensors_app.extensions.settingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class DataStoreManager @Inject constructor(context: Context) {

    private val settings: DataStore<Preferences> = context.settingsDataStore

    val settingsFlow: Flow<Preferences> = settings.data
        .catch { flowOf(null) }

//    suspend fun updateSettings() {
//        settings.updateData {
//
//        }
//    }

}
