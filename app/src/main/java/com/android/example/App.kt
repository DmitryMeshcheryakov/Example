package com.android.example

import com.android.example.core.app.BaseApplication
import com.android.example.core.common.Logger
import com.android.example.core.modules.AppModule
import com.android.example.core.modules.location.LocationModule
import com.android.example.core.modules.preferences.PreferencesModule
import com.android.example.core.modules.resources.ResourcesModule
import com.android.example.core.modules.vibro.VibroModule
import com.android.example.features.common.Log
import com.android.example.features.modules.AppComponent
import com.android.example.features.modules.DaggerAppComponent
import org.greenrobot.eventbus.EventBus

class App : BaseApplication() {

    companion object {
        var component: AppComponent? = null
            private set

        lateinit var bus: EventBus
        fun component() = component as AppComponent
        val log: Logger
            get() = logger
    }

    init {
        logger = Log
    }

    override fun onCreate() {
        super.onCreate()
        bus = EventBus.getDefault()

        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .preferencesModule(PreferencesModule(this))
            .resourcesModule(ResourcesModule(this))
            .locationModule(LocationModule(this))
            .vibroModule(VibroModule(this))
            .build()
    }
}