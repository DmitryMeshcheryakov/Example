package com.android.example.core.features.confirmation

import com.android.example.core.features.confirmation.model.Confirmation
import com.android.example.core.features.confirmation.model.ConfirmationType
import com.android.example.core.features.confirmation.model.orDefault
import com.android.example.core.repository.BaseInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class BaseConfirmationInteractor @Inject constructor() : BaseInteractor() {

    @Inject
    lateinit var confirmationCacheDataSource: ConfirmationCacheDataSource

    open fun putConfirmations(list: List<Confirmation>) {
        confirmationCacheDataSource.put(list)
    }

    open fun getConformation(url: String): ConfirmationType {
        if (url.isEmpty()) return ConfirmationType.NO_CONFIRM
        val confirmation = confirmationCacheDataSource.get()?.firstOrNull { it.url.contains(url) }
        return if (confirmation != null) confirmation.type.orDefault()
        else ConfirmationType.NO_CONFIRM
    }

    override fun doOnExit() {
        confirmationCacheDataSource.clear()
    }
}