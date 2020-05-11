package com.android.example.core.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.android.example.core.app.BaseApplication
import com.android.example.core.features.settings.Prefs
import java.util.*

object LocaleUtils: Prefs<Locale> {

    private const val KEY = "APP_LOCALE"

    private var isSystem: Boolean = true
    private var needUpdate: Boolean = true

    override var value: Locale = Locale.getDefault()

    override fun put(context: Context?, value: Locale) {
        val prefs = context?.getSharedPreferences(
            "mobileres04kv",
            Context.MODE_PRIVATE
        )
        if (prefs?.edit()?.putString(KEY, value.language)?.commit() == true) {
            Locale.setDefault(value)
            BaseApplication.logger.i(KEY, "save $value")
            this.value = value
        }
    }

    override fun get(context: Context?): Locale {
        val prefs = context?.getSharedPreferences(
            "mobileres04kv",
            Context.MODE_PRIVATE
        )
        val fromPrefs = prefs?.getString(KEY, "")
        val fromDef = "ru"
        val locale = Locale(
            when {
                !fromPrefs.isNullOrEmpty() -> {
                    isSystem = false
                    fromPrefs
                }
                !fromDef.isNullOrEmpty() -> {
                    isSystem = false
                    fromDef
                }
                else -> {
                    isSystem = true
                    Locale.getDefault().language
                }
            }
        )

        BaseApplication.logger.i(KEY, "isSystem: $isSystem; fromPrefs: $fromPrefs; fromDef: $fromDef; result: $locale")

        BaseApplication.logger.i(KEY, "get $locale")
        return locale
    }

    override fun reset(context: Context?) {
        val prefs = context?.getSharedPreferences("mobileres04kv", Context.MODE_PRIVATE)
        if (prefs?.edit()?.remove(KEY)?.commit() == true) {
            BaseApplication.logger.i(KEY, "reset")
            this.value = get(context)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        BaseApplication.logger.i(KEY, "onConfigurationChanged")
        needUpdate = true
        if (isSystem) {
            value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) newConfig.locales.get(0)
            else newConfig.locale
        }
    }

    fun attachBaseContext(context: Context?, forceUpdate: Boolean = true): Context? {
        BaseApplication.logger.i(KEY, "attachBaseContext")
        if (needUpdate || forceUpdate) {
            try {
                val resources = context?.resources
                val configuration = Configuration(resources?.configuration)

                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    configuration.setLocale(value)
                    Locale.setDefault(value)
                    BaseApplication.logger.i(KEY, "apply $value")
                    needUpdate = false
                    context?.createConfigurationContext(configuration)
                } else {
                    configuration.locale = value
                    resources?.updateConfiguration(configuration, resources.displayMetrics)
                    Locale.setDefault(value)
                    BaseApplication.logger.i(KEY, "apply $value")
                    needUpdate = false
                    context
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return context
    }
}