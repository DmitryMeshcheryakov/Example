package com.android.example.core.common

import android.util.Log

interface Logger {

    var enableLog: Boolean

    fun v(tag: String, vararg messages: Any?) {
        if (enableLog) {
            messages.forEach {
                Log.v(tag, it.toString())
            }
        }
    }

    fun d(tag: String, vararg messages: Any?) {
        if (enableLog) {
            messages.forEach {
                Log.d(tag, it.toString())
            }
        }
    }

    fun i(tag: String, vararg messages: Any?) {
        if (enableLog) {
            messages.forEach {
                Log.i(tag, it.toString())
            }
        }
    }

    fun e(tag: String, vararg messages: Any?) {
        if (enableLog) {
            messages.forEach {
                Log.e(tag, it.toString())
            }
        }
    }

    fun e(t: Throwable) {
        if (enableLog)
            t.printStackTrace()
    }

    fun network(message: String) {
        if (enableLog)
            Log.e("HTTP", message)
    }
}