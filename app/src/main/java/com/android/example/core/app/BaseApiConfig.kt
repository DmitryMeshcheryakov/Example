package com.android.example.core.app

import com.android.example.core.utils.LocaleUtils

abstract class BaseApiConfig {

    open val mockData: Map<String, String>? = null

    open val connectionTimeout: Long = 60 * 1000 // 1 min

    open val readTimeout: Long = 60 * 1000 // 1 min

    open val writeTimeout: Long = 60 * 1000 // 1 min

    open val certificatePins: List<String> = emptyList()

    open val defaultHeaders: Map<String, String> = mapOf(
        "Accept" to "application/json",
        "Accept-Encoding" to "gzip,deflate",
        "Content-Type" to "application/json",
        "language" to LocaleUtils.value.language
    )

    abstract val baseUrl: String
}