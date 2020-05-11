package com.android.example.core.features.settings

import android.content.Context
import android.content.res.Configuration

interface Prefs<V> {

    var value: V

    fun put(context: Context?, value: V)

    fun get(context: Context?): V

    fun reset(context: Context?)

    fun onConfigurationChanged(newConfig: Configuration)
}