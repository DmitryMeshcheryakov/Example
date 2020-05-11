package com.android.example.core.features.pin

import com.android.example.core.features.pin.model.Pin
import com.android.example.core.repository.BaseDataSource
import com.android.example.core.repository.LocalRepository
import javax.inject.Inject

class PinDataSource @Inject constructor() : BaseDataSource<Pin>(), LocalRepository<Pin> {

    override fun get(vararg args: Any?): Pin {
        val pin = preferences.get().getString(PIN)
        val iv = preferences.get().getString(ENCRYPTION_IV)

        if (pin.isNullOrEmpty() || iv.isNullOrEmpty())
            throw IllegalStateException("pin is empty")

        return Pin(pin, iv)
    }

    override fun put(entity: Pin?) {
        entity?.let {
            if (!preferences.get().putString(PIN, it.pin) || !preferences.get().putString(ENCRYPTION_IV, it.iv))
                throw IllegalStateException("cannot save pin")
        }
    }

    override fun clear() {
        preferences.get().remove(PIN)
        preferences.get().remove(ENCRYPTION_IV)
    }

    companion object {
        private const val PIN = "p_fp_"
        private const val ENCRYPTION_IV = "e_fp_"
    }
}