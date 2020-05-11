package com.android.example.features.modules

import com.android.example.core.modules.AppModule
import com.android.example.core.modules.location.LocationModule
import com.android.example.core.modules.preferences.PreferencesModule
import com.android.example.core.modules.resources.ResourcesModule
import com.android.example.core.modules.vibro.VibroModule
import com.android.example.features.mvp.ScreenManager
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        (ResourcesModule::class),
        (PreferencesModule::class),
        (AppModule::class),
        (LocationModule::class),
        (VibroModule::class)
    ]
)
interface AppComponent {
    fun inject(screenManager: ScreenManager)
}