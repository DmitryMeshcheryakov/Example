package com.android.example.core.modules.files

import android.content.Context
import com.android.example.core.modules.files.FileHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FileModule(private val context: Context) {

    @Provides
    @Singleton
    internal fun provideFileHelper(): FileHelper {
        return FileHelper(context)
    }
}