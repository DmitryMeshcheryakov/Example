package com.android.example.core.features.autofill

import com.android.example.core.features.autofill.model.BaseAutoFillItem
import com.android.example.core.repository.BaseDataSource
import com.android.example.core.repository.LocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutoFillCacheDataSource<A : BaseAutoFillItem> @Inject constructor() : BaseDataSource<List<A>>(),
    LocalRepository<List<A>> {

    private var data: List<A>? = null

    override fun get(vararg args: Any?): List<A>? {
        return data
    }

    override fun put(entity: List<A>?) {
        data = entity
    }
}