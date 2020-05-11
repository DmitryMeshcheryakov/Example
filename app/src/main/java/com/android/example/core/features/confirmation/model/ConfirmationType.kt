package com.android.example.core.features.confirmation.model

import java.util.*

enum class ConfirmationType {
    SMS,
    CODE_WORD,
    SECURE_3D,
    NO_CONFIRM;

    companion object {
        fun fromString(str: String?): ConfirmationType {
            return try {
                valueOf(str?.toUpperCase() ?: "NO_CONFIRM")
            } catch (e: Exception) {
                NO_CONFIRM
            }
        }
    }
}

fun ConfirmationType?.orDefault(): ConfirmationType = this ?: ConfirmationType.CODE_WORD

val String?.confirmationType: ConfirmationType?
    get() {
        return kotlin.runCatching {
            ConfirmationType.valueOf(this.orEmpty().toUpperCase(Locale.getDefault()))
        }.getOrNull()
    }