package com.android.example.core.modules.vibro

import android.content.Context
import com.android.example.core.modules.vibro.VibroHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VibroModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideVibroHelper(): VibroHelper {
        return VibroHelper(context)
    }
}