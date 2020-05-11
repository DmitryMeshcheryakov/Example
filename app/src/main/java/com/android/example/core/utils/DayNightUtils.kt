package com.android.example.core.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import androidx.appcompat.app.AppCompatDelegate
import com.android.example.core.app.BaseApplication
import com.android.example.core.features.settings.Prefs

object DayNightUtils : Prefs<Int> {

    private const val KEY = "NIGHT_MODE"

    fun isDarkModeEnabled(context: Context?): Boolean {
        WebView(context)
        return context?.let {
            when (it.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                else -> false
            }
        } ?: false
    }

    override var value: Int = AppCompatDelegate.MODE_NIGHT_NO
        set(value) {
            field = value
            Handler(Looper.getMainLooper()).post {
                AppCompatDelegate.setDefaultNightMode(value)
                BaseApplication.logger.i(KEY, "apply $value")
            }
        }

    override fun put(context: Context?, value: Int) {
        val prefs = context?.getSharedPreferences(
            "mobileres04kv",
            Context.MODE_PRIVATE
        )
        if (prefs?.edit()?.putInt(KEY, value)?.commit() == true) {
            BaseApplication.logger.i(KEY, "save $value")
            this.value = value
            updateWebView(context)
        }
    }

    override fun get(context: Context?): Int {
        val prefs = context?.getSharedPreferences(
            "mobileres04kv",
            Context.MODE_PRIVATE
        )

        val mode = when (val fromPref = prefs?.getInt(KEY, -20)) {
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES,
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> fromPref
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }

        updateWebView(context)
        BaseApplication.logger.i(KEY, "get $mode")
        return mode
    }

    override fun reset(context: Context?) {
        val prefs = context?.getSharedPreferences(
            "mobileres04kv",
            Context.MODE_PRIVATE
        )
        if (prefs?.edit()?.remove(KEY)?.commit() == true) {
            BaseApplication.logger.i(KEY, "reset")
            put(context, AppCompatDelegate.MODE_NIGHT_NO)
        }
        updateWebView(context)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        BaseApplication.logger.i(KEY, "onConfigurationChanged")
        if (value != AppCompatDelegate.MODE_NIGHT_NO && value != AppCompatDelegate.MODE_NIGHT_YES) {
            when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
                Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }
        }
    }

    private fun updateWebView(context: Context?) {
        runCatching {
            Handler(Looper.getMainLooper()).post {
                WebView(context)
            }
        }
    }
}