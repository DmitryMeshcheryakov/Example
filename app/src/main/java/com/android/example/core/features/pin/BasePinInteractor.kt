package com.android.example.core.features.pin

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.android.example.core.features.pin.model.Pin
import com.android.example.core.modules.deviceinfo.DeviceInfo
import com.android.example.core.repository.BaseInteractor
import dagger.Lazy
import java.nio.charset.Charset
import java.security.KeyStore
import java.security.MessageDigest
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec
import javax.inject.Inject

open class BasePinInteractor @Inject constructor() : BaseInteractor() {

    @Inject
    lateinit var deviceInfo: Lazy<DeviceInfo>

    @Inject
    lateinit var biometricDataSource: PinDataSource

    companion object {
        private const val AUTHENTICATION_DURATION_SECONDS = 30
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val KEY_NAME = "my_key"
        private const val CHARSET_NAME = "UTF-8"

        private const val ALGORITHM = "PBEWithMD5AndDES"
        private const val ITERATION_COUNT = 20
    }

    open fun getPin(): String {
        val pin = biometricDataSource.get()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cipher = getCipherForDecrypt()
            val encryptedPassword = Base64.decode(pin.pin, Base64.DEFAULT)
            val passwordBytes = cipher.doFinal(encryptedPassword)
            String(passwordBytes, Charset.forName(CHARSET_NAME))
        } else {
            val key = "pinCryptoKey" + deviceInfo.get().deviceUDID + pin.iv
            decrypt(pin.pin, key)
        }
    }

    open fun setPin(pin: String) {
        val p = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cipher =
                Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
            cipher.init(Cipher.ENCRYPT_MODE, getKey())
            val encryptionIv = cipher.iv
            val encryptedPin =
                Base64.encodeToString(cipher.doFinal(pin.toByteArray(charset(CHARSET_NAME))), Base64.DEFAULT)
            Pin(encryptedPin, Base64.encodeToString(encryptionIv, Base64.DEFAULT))
        } else {
            val iv = UUID.randomUUID().toString()
            val key = "pinCryptoKey" + deviceInfo.get().deviceUDID + iv
            Pin(crypt(pin, key), iv)
        }
        biometricDataSource.put(p)
    }

    open fun isPinCorrect(pin: String?): Boolean {
        val currentPin = getPin()
        return currentPin == pin
    }

    fun getCipherForDecrypt(): Cipher {
        val pin = biometricDataSource.get()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)
            val secretKey = keyStore.getKey(KEY_NAME, null) as SecretKey
            val cipher =
                Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
            val encryptionIv = Base64.decode(pin.iv, Base64.DEFAULT)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(encryptionIv))
            cipher
        } else
            Cipher.getInstance(ALGORITHM)
    }

    private fun getKey(): SecretKey? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(false)
                    .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_DURATION_SECONDS)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build()
            )
            keyGenerator.generateKey()
        } else null
    }

    override fun doOnLogout() = biometricDataSource.clear()

    override fun doOnExit() {}

    private fun crypt(str: String, seed: String): String {
        val keyFactory = SecretKeyFactory
            .getInstance(ALGORITHM)
        val key: SecretKey
        key = keyFactory.generateSecret(PBEKeySpec(seed.toCharArray()))

        val pbeCipher = Cipher.getInstance(ALGORITHM)
        pbeCipher.init(
            Cipher.ENCRYPT_MODE, key,
            PBEParameterSpec(
                seed.toByteArray(),
                ITERATION_COUNT
            )
        )
        return toBase64(pbeCipher.doFinal(str.toByteArray()))
    }

    private fun decrypt(str: String, seed: String): String {
        val keyFactory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val key = keyFactory.generateSecret(PBEKeySpec(seed.toCharArray()))
        val pbeCipher = Cipher.getInstance(ALGORITHM)
        pbeCipher.init(
            Cipher.DECRYPT_MODE, key,
            PBEParameterSpec(
                seed.toByteArray(),
                ITERATION_COUNT
            )
        )
        return String(pbeCipher.doFinal(fromBase64(str)))
    }

    protected fun getHash(str: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(str.toByteArray(charset("UTF-8")))
        val hexString = StringBuffer()

        for (i in hash.indices) {
            val hex = Integer.toHexString(0xff and hash[i].toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }

        return hexString.toString()
    }

    protected fun toBase64(str: ByteArray): String {
        return Base64.encodeToString(str, Base64.DEFAULT)
    }

    protected fun fromBase64(str: String): ByteArray {
        return Base64.decode(str, Base64.DEFAULT)
    }
}