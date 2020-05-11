package com.android.example.core.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.android.example.core.common.Logger
import com.android.example.core.utils.DayNightUtils
import com.android.example.core.utils.LocaleUtils

@SuppressLint("Registered")
abstract class BaseApplication : Application() {

    companion object {
        lateinit var logger: Logger

        lateinit var component: BaseAppComponent
    }

    override fun onCreate() {
        super.onCreate()
        DayNightUtils.value = DayNightUtils.get(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        newConfig.let { LocaleUtils.onConfigurationChanged(it) }
    }

    override fun attachBaseContext(base: Context) {
        LocaleUtils.value = LocaleUtils.get(base)
        super.attachBaseContext(LocaleUtils.attachBaseContext(base) ?: base)
    }
}