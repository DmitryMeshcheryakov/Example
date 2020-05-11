package com.android.example.core.modules.deviceinfo

import android.content.Context
import com.android.example.core.modules.deviceinfo.DeviceInfo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceInfoModule(private val context: Context) {

    @Provides
    @Singleton
    internal fun provideDeviceInfo(): DeviceInfo {
        return DeviceInfo(context)
    }
}