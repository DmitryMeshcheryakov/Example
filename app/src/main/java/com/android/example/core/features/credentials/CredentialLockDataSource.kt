package com.android.example.core.features.credentials

import com.android.example.core.repository.BaseDataSource
import com.android.example.core.repository.LocalRepository
import com.google.gson.annotations.SerializedName
import java.util.*
import javax.inject.Inject

class CredentialLockDataSource @Inject constructor() : BaseDataSource<Pair<String?, Boolean>>(), LocalRepository<Pair<String?, Boolean>> {


    override fun get(vararg args: Any?): Pair<String?, Boolean> {
        val login = (args[0] as? String?).orEmpty()
        val data = kotlin.runCatching { preferences.get().getObject<Dto>(KEY) }.getOrNull()?.locks
        val isLocked = data?.filter { it.lockDate + 60000 > Date().time }
            ?.map { it.login }
            ?.contains(login) ?: false

        return Pair(login, isLocked)
    }

    override fun put(entity: Pair<String?, Boolean>?) {
        entity?.let {
            val login = it.first
            val lock = it.second

            var data = kotlin.runCatching { preferences.get().getObject<Dto>(KEY) }
                .getOrNull()?.locks
                .orEmpty()
                .filter { it.lockDate + 60000 > Date().time }

            if (lock && !login.isNullOrEmpty()) data = data.plus(Dto.LockDto(login, Date().time))
            else if (!login.isNullOrEmpty()) data = data.filterNot { it.login == login }

            preferences.get().putObject(KEY, Dto(data))
        }
    }

    override fun clear() {
        kotlin.runCatching {
            preferences.get().remove(KEY)
        }
    }

    companion object {
        private const val KEY = "userBlock"
    }

    class Dto(
        @SerializedName("locks") var locks: List<LockDto>
    ) {
        class LockDto(
            @SerializedName("login") var login: String,
            @SerializedName("lockDate") var lockDate: Long
        )
    }
}