package com.android.example.core.features.credentials

import com.android.example.core.exceptions.CredentialLockException
import com.android.example.core.features.credentials.model.Credentials
import com.android.example.core.features.pin.BasePinInteractor
import org.json.JSONArray
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec
import javax.inject.Inject

open class BaseCredentialsInteractor @Inject constructor() : BasePinInteractor() {

    @Inject
    lateinit var credentialDataSource: CredentialsDataSource

    @Inject
    lateinit var credentialLockDataSource: CredentialLockDataSource

    open fun isUserBlocked(login: String?) = credentialLockDataSource.get(login).second

    open fun blockUser(login: String?, block: Boolean) = credentialLockDataSource.put(login to block)

    open fun isUserLogined(): Boolean = kotlin.runCatching { credentialDataSource.get() }.getOrNull() != null

    open fun getLogin() = credentialDataSource.get().login

    open fun getPassword(pin: String?): String {
        val currentPin = credentialDataSource.get()
        val key = "pinCryptoKey" + deviceInfo.get().deviceUDID + pin

        return JSONArray(decrypt(currentPin.val1, key)).optString(0, null)
    }

    open fun changePin(newPin: String?) {
        val pin = getPin()
        val pass = getPassword(pin)
        val login = getLogin()

        setCredentials(newPin, login, pass)
    }

    open fun changePassword(newPassword: String?) {
        val pin = getPin()
        val login = getLogin()

        setCredentials(pin, login, newPassword)
    }

    open fun changeLogin(newLogin: String?) {
        val pin = getPin()
        val pass = getPassword(pin)

        setCredentials(pin, newLogin, pass)
    }

    open fun setCredentials(pin: String?, login: String?, pass: String?) {
        if (isUserBlocked(login))
            throw CredentialLockException("couldn't save new pin for locked credentials")

        if (pin.isNullOrEmpty() || pass.isNullOrEmpty() || login.isNullOrEmpty())
            throw IllegalArgumentException("pin, login or pass is empty")

        val key = "pinCryptoKey" + deviceInfo.get().deviceUDID + pin
        val json: String = JSONArray().put(pass).toString()

        val s1 = crypt(json, key)
        val s2 = getHash(pin + "pinCryptoKey")

        if (s1.isEmpty() || s2.isEmpty())
            throw IllegalStateException("crypto error")

        credentialDataSource.put(Credentials(s1, s2, login))
        setPin(pin)
    }

    override fun setPin(pin: String) {
        if (isUserBlocked(getLogin()))
            throw CredentialLockException("couldn't save pin for locked credentials")

        super.setPin(pin)
    }

    override fun getPin(): String {
        if (isUserBlocked(getLogin()))
            throw CredentialLockException("couldn't get pin for locked credentials")

        return super.getPin()
    }

    override fun isPinCorrect(pin: String?): Boolean {
        if (isUserBlocked(getLogin()))
            throw CredentialLockException("couldn't check pin for locked credentials")

        return super.isPinCorrect(pin)
    }

    override fun doOnLogout() {
        super.doOnLogout()
        credentialDataSource.clear()
    }

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

    companion object {
        private const val ALGORITHM = "PBEWithMD5AndDES"
        private const val ITERATION_COUNT = 20
    }
}