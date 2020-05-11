package com.android.example.core.modules.biometric

import android.app.KeyguardManager
import android.content.Context
import android.os.Handler
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.android.example.R
import javax.crypto.Cipher

class BiometricHelperV28(val context: Context?) : BiometricHelper {


    companion object {
        private const val DELAY_BEFORE_SCAN = 300L //ms
    }

    private var keyguardManager: KeyguardManager? = null
    private var fingerprintManager: FingerprintManagerCompat? = null
    private var cancellationSignal: CancellationSignal? = null

    init {
        context?.let {
            keyguardManager = it.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            fingerprintManager = FingerprintManagerCompat.from(it)
        }
    }

    override fun isDeviceSupportBiometric(): Boolean {
        return fingerprintManager?.isHardwareDetected == true
    }

    override fun canUseBiometric(): Boolean {
        return isDeviceSupportBiometric() && fingerprintManager?.hasEnrolledFingerprints() == true && keyguardManager?.isKeyguardSecure == true
    }

    override fun scan(cipher: Cipher, listener: ((cipher: Cipher?, status: BiometricStatus, description: String?) -> Unit)?) {
        listener?.invoke(null, BiometricStatus.ALERT, context?.getString(R.string.init).orEmpty())
        Handler().postDelayed({
            cancellationSignal = CancellationSignal()
            fingerprintManager?.authenticate(FingerprintManagerCompat.CryptoObject(cipher), 0, cancellationSignal, object : FingerprintManagerCompat.AuthenticationCallback() {
                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
                    listener?.invoke(null, BiometricStatus.ALERT, if (helpString.isEmpty()) null else helpString.toString())
                }

                override fun onAuthenticationFailed() {
                    listener?.invoke(null, BiometricStatus.ERROR,null)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    listener?.invoke(null, BiometricStatus.ERROR, if (errString.isEmpty()) null else errString.toString())
                }

                override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult) {
                    listener?.invoke(result.cryptoObject.cipher, BiometricStatus.SUCCESS, null)
                }
            }, null)
            listener?.invoke(null, BiometricStatus.ALERT, "")
        }, DELAY_BEFORE_SCAN)
    }

    override fun exitScan() {
        cancellationSignal?.cancel()
        cancellationSignal = null
    }
}