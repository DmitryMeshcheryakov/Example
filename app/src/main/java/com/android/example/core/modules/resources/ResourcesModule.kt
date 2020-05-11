package com.android.example.core.modules.resources

import android.content.Context
import com.android.example.core.modules.resources.ResourcesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ResourcesModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideResources(): ResourcesHelper {
        return ResourcesHelper(context)
    }
}