package com.android.example.core.modules.preferences

import android.content.Context
import com.android.example.core.modules.preferences.PreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule(private val context: Context) {

    @Provides
    @Singleton
    internal fun providePreferences(): PreferencesHelper {
        return PreferencesHelper(context.getSharedPreferences("example", Context.MODE_PRIVATE))
    }
}