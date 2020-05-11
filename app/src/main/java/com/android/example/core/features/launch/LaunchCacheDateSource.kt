package com.android.example.core.features.launch

import com.android.example.core.features.launch.model.LaunchItem
import com.android.example.core.repository.BaseDataSource
import com.android.example.core.repository.LocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchCacheDateSource @Inject constructor() : BaseDataSource<LaunchItem<*>>(),
    LocalRepository<LaunchItem<*>> {

    private var data: LaunchItem<*>? = null

    override fun get(vararg args: Any?): LaunchItem<*>? {
        return data
    }

    override fun put(entity: LaunchItem<*>?) {
        data = entity
    }
}