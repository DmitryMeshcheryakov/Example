package com.android.example.core.modules.biometric

import android.content.Context
import android.os.Build
import com.android.example.core.modules.biometric.BiometricHelper
import com.android.example.core.modules.biometric.BiometricHelperV23
import com.android.example.core.modules.biometric.BiometricHelperV28
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BiometricModule(private val context: Context) {

    @Provides
    @Singleton
    internal fun provideBiometric(): BiometricHelper {
        return kotlin.runCatching {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> BiometricHelperV23(context)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> BiometricHelperV28(context)
                else -> object : BiometricHelper {}
            }
        }.getOrNull() ?: object : BiometricHelper {}
    }
}