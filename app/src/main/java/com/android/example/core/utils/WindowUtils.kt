package com.android.example.core.utils

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils

object WindowUtils {

    fun setStatusBarColor(
        activity: Activity?,
        @ColorInt color: Int,
        darkIcons: Boolean? = null
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity?.window
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor = color

            val decor = window?.decorView
            var flags = activity?.window?.decorView?.systemUiVisibility ?: 0
            flags = if (darkIcons ?: isDark(color))
                flags and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            else
                flags or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            decor?.systemUiVisibility = flags
        }
    }

    fun setNavigationBarColor(
        activity: Activity?,
        @ColorInt color: Int,
        darkIcons: Boolean? = null
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val window = activity?.window
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.navigationBarColor = color

            val decor = window?.decorView
            var flags = activity?.window?.decorView?.systemUiVisibility ?: 0

            flags = if (darkIcons ?: isDark(color))
                flags and SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            else
                flags or SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

            decor?.systemUiVisibility = flags
        }

    }

    fun hideSoftKeyboard(activity: Activity?) {
        Handler(Looper.getMainLooper()).post {
            runCatching {
                ContextCompat.getSystemService(activity!!, InputMethodManager::class.java)
                    ?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            }
        }
    }

    fun showSoftKeyboard(activity: Activity?, view: View? = null) {
        Handler(Looper.getMainLooper()).post {
            runCatching {
                ContextCompat.getSystemService(activity!!, InputMethodManager::class.java)
                    ?.showSoftInput(view ?: activity.currentFocus, SHOW_FORCED)
            }
        }
    }

    fun toggleSoftKeyboard(activity: Activity?) {
        Handler(Looper.getMainLooper()).post {
            runCatching {
                ContextCompat.getSystemService(activity!!, InputMethodManager::class.java)
                    ?.toggleSoftInput(SHOW_FORCED, HIDE_NOT_ALWAYS)
            }
        }
    }

    fun setSecure(activity: Activity?, secure: Boolean) {
        if (secure) activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        else activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    private fun isDark(@ColorInt color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }

}