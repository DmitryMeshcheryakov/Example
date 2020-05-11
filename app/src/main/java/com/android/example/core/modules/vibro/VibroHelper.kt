package com.android.example.core.modules.vibro

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat

class VibroHelper(context: Context) {

    companion object {
        const val VIBRATE_SHORT = 30L
        const val VIBRATE_LONG = 300L
        const val PAUSE_SHORT = 70L
        const val PAUSE_LONG = 200L
    }

    private var vibrator: Vibrator? = ContextCompat.getSystemService(context, Vibrator::class.java)

    fun vibrateShort() {
        vibrate(longArrayOf(0, VIBRATE_SHORT))
    }

    fun vibrateLong() {
        vibrate(longArrayOf(0, VIBRATE_LONG))
    }

    fun vibrateDoubleShort() {
        vibrate(longArrayOf(0, VIBRATE_SHORT, PAUSE_SHORT, VIBRATE_SHORT))
    }

    fun vibrate(pattern: LongArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var silent = true
            var amplitudes = emptyList<Int>()
            pattern.forEach {
                amplitudes = amplitudes.plus(if (silent) 0 else 255)
                silent = !silent
            }
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, amplitudes.toIntArray(), -1))
        } else {
            vibrator?.vibrate(pattern, -1)
        }
    }
}