package com.android.example.core.modules.location

import android.content.Context
import com.android.example.core.modules.location.LocationHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocationModule (val context: Context) {

    @Provides
    @Singleton
    fun provideLocation(): LocationHelper {
        return LocationHelper(context)
    }
}