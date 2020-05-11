package com.android.example.core.modules.biometric

import javax.crypto.Cipher

interface BiometricHelper {

    fun isDeviceSupportBiometric(): Boolean = false

    fun canUseBiometric(): Boolean = false

    fun scan(cipher: Cipher, listener: ((cipher: Cipher?, status: BiometricStatus, description: String?) -> Unit)? = null) {}

    fun exitScan() {}
}