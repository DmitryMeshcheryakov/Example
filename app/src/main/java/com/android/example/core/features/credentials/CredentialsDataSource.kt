package com.android.example.core.features.credentials

import com.android.example.core.features.credentials.model.Credentials
import com.android.example.core.repository.BaseDataSource
import com.android.example.core.repository.LocalRepository
import javax.inject.Inject

class CredentialsDataSource @Inject constructor() : BaseDataSource<Credentials>(), LocalRepository<Credentials> {

    override fun get(vararg args: Any?): Credentials {

        val val1 = preferences.get().getString(KEY_SAVE)
        val val2 = preferences.get().getString(KEY_SAVE1)
        val login = preferences.get().getString(KEY_LOGIN)

        if (val1.isNullOrEmpty() || val2.isNullOrEmpty() || login.isNullOrEmpty())
            throw IllegalStateException("pin is null")

        return Credentials(val1, val2, login)
    }

    override fun put(entity: Credentials?) {
        if (entity == null)
            throw IllegalArgumentException("credentials null")

        if (!preferences.get().putString(KEY_SAVE, entity.val1))
            throw IllegalStateException("s1 is not saved")

        if (!preferences.get().putString(KEY_SAVE1, entity.val2))
            throw IllegalStateException("s2 is not saved")

        if (!preferences.get().putString(KEY_LOGIN, entity.login))
            throw IllegalStateException("login is not saved")
    }

    override fun clear() {
        preferences.get().remove(KEY_SAVE)
        preferences.get().remove(KEY_SAVE1)
        preferences.get().remove(KEY_LOGIN)
    }

    companion object {
        private const val KEY_SAVE = "s_key_"
        private const val KEY_SAVE1 = "s_1key_"
        private const val KEY_LOGIN = "login"
    }
}