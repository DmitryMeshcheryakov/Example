package com.android.example.core.features.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.android.example.core.modules.biometric.BiometricHelper
import com.android.example.core.repository.BaseInteractor
import com.android.example.core.utils.DayNightUtils
import com.android.example.core.utils.LocaleUtils
import java.util.*
import javax.inject.Inject

open class BaseSettingsInteractor @Inject constructor() : BaseInteractor() {

    @Inject
    lateinit var biometricModule: BiometricHelper

    @Inject
    lateinit var context: Context

    open fun setLocale(locale: Locale) {
        LocaleUtils.put(context, locale)
    }

    open fun getLocale(): Locale {
        return runCatching { LocaleUtils.get(context) }
            .getOrDefault(Locale.getDefault())
    }

    open fun getAvailableLocales(): Set<Locale> {
        return setOf(
            Locale("ru"),
            Locale("en"),
            Locale("by")
        )
    }

    open fun setNightMode(@AppCompatDelegate.NightMode mode: Int) {
        DayNightUtils.put(context, mode)
    }

    open fun getNightMode(): Int {
        return runCatching { DayNightUtils.get(context) }
            .getOrDefault(AppCompatDelegate.MODE_NIGHT_NO)
    }

    open fun getAvailableNightModes(): Set<Int> {
        return setOf(
            AppCompatDelegate.MODE_NIGHT_YES,
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
    }

    open fun setUseBiometricForAuth(use: Boolean) {
        preferences.get().putBool(KEY_USE_BIOMETRIC, use)
    }

    open fun useBiometricForAuth(): Boolean {
        return runCatching {
            biometricModule.canUseBiometric()
                    && biometricModule.isDeviceSupportBiometric()
                    && preferences.get().getBool(KEY_USE_BIOMETRIC, false)
        }
            .getOrDefault(false)
    }

    override fun doOnLogout() {
        super.doOnLogout()
        runCatching {
            preferences.get().remove(KEY_USE_BIOMETRIC)
            LocaleUtils.reset(context)
            DayNightUtils.reset(context)
        }
    }

    override fun doOnExit() {}

    companion object {
        private const val KEY_USE_BIOMETRIC = "use_biometric_for_auth"
    }
}