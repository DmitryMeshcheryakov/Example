package com.android.example.core.repository

import com.android.example.core.modules.preferences.PreferencesHelper
import com.android.example.core.modules.resources.ResourcesHelper
import dagger.Lazy
import javax.inject.Inject

abstract class BaseInteractor {

    @Inject
    lateinit var resources: Lazy<ResourcesHelper>

    @Inject
    lateinit var preferences: Lazy<PreferencesHelper>

    open fun doOnLogout() = doOnExit()

    abstract fun doOnExit()

    protected fun <Object>get(api: () -> Object, local: LocalRepository<Object>, forceLoadData: Boolean, vararg args: Any?): Object {
        val localData = local.get(*args)
        if (localData == null || forceLoadData) {
            val data = api.invoke()
            local.put(data)
            return data!!
        }
        return localData
    }

    protected fun <Object>put(api: () -> Unit, local: LocalRepository<Object>, entity: Object, vararg args: Any?) {
        api.invoke()
        local.put(entity)
    }

    protected fun <Object>update(api: () -> Unit, local: LocalRepository<Object>, entity: Object, vararg args: Any?) {
        api.invoke()
        local.update(entity, *args)
    }

    protected fun <Object>remove(api: () -> Unit, local: LocalRepository<Object>, vararg args: Any?) {
        api.invoke()
        local.remove(*args)
    }
}