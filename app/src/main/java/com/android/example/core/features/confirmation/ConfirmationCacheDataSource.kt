package com.android.example.core.features.confirmation

import com.android.example.core.features.confirmation.model.Confirmation
import com.android.example.core.repository.BaseDataSource
import com.android.example.core.repository.LocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfirmationCacheDataSource @Inject constructor(): BaseDataSource<List<Confirmation>>(), LocalRepository<List<Confirmation>> {

    private var data: List<Confirmation>? = null

    override fun get(vararg args: Any?): List<Confirmation>? {
        return data
    }

    override fun put(entity: List<Confirmation>?) {
        data = entity
    }

}