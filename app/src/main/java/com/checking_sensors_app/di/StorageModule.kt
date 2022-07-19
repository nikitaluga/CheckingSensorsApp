package com.checking_sensors_app.di

import android.content.Context
import com.checking_sensors_app.data.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    fun dataStore(context: Context) = DataStoreManager(context)
}