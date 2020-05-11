package com.android.example.core.repository

import com.android.example.core.modules.preferences.PreferencesHelper
import com.android.example.core.modules.resources.ResourcesHelper
import dagger.Lazy
import javax.inject.Inject

abstract class BaseDataSource<Data>: LocalRepository<Data> {

    @Inject
    lateinit var preferences: Lazy<PreferencesHelper>

    @Inject
    lateinit var resources: Lazy<ResourcesHelper>
}