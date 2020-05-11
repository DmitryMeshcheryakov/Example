package com.android.example.features.mvp

import com.android.example.core.repository.BaseInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

abstract class BaseAppInteractor: BaseInteractor() {

}

fun <T> BaseAppInteractor.async(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return CoroutineScope(Dispatchers.IO).async(block = block)
}