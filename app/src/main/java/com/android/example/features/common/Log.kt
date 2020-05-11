package com.android.example.features.common

import com.android.example.core.common.Logger

object Log : Logger {
    override var enableLog: Boolean = true//BuildConfig.SERVER_TYPE == "test"

}