package com.android.example.core.app

import android.widget.ImageView
import com.android.example.core.features.confirmation.model.ConfirmationType

abstract class BaseAppConfig {

    open var sessionLifeTime : Long = 5 * 60 * 1000L // 5 min

    open val userLockInterval = 15 * 60 * 1000 // 15 min

    open val formatConfig: BaseFormatConfig = BaseFormatConfig()

    open val fileProviderAuth: String = ""

    open val defaultAppLocale: String? = "ru"

    open var defaultConfirmation: ConfirmationType = ConfirmationType.CODE_WORD

    open fun loadImage(view: ImageView?, image: Any?) {}

    abstract val apiConfig: BaseApiConfig

    abstract val preferencesName : String

    abstract val pinCryptoKey : String

    abstract val bankName: String
}