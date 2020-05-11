package com.android.example.core.features.autofill

import com.android.example.core.app.BaseApplication
import com.android.example.core.features.autofill.model.BaseAutoFillItem
import com.android.example.core.repository.BaseInteractor
import javax.inject.Inject

abstract class BaseAutoFillInteractor<A : BaseAutoFillItem> : BaseInteractor() {

    @Inject
    lateinit var autofillCacheDataSource: AutoFillCacheDataSource<A>

    open fun putAutoFill(list: List<A>) {
        BaseApplication.logger.i(LOG_TAG, "put ${list.map { "${it.id}: ${it.value}" }}")
        autofillCacheDataSource.put(list)
    }

    open fun putAutoFill(item: A?) {
        item?.let { putAutoFill(listOf(it)) }
    }

    open fun isEmpty(): Boolean {
        return autofillCacheDataSource.get().isNullOrEmpty()
    }

    open fun clear() {
        doOnExit()
    }

    open fun getAmount(): Double? {
        return autofillCacheDataSource.get()?.firstOrNull { it.id == amountId }
            ?.value?.toDoubleOrNull()
    }

    open fun getFirstField(): String? {
        return autofillCacheDataSource.get()
            ?.firstOrNull { it.id == FIRST_FIELD_ID }
            ?.value
    }

    open fun getPaymentSource(): String? {
        return autofillCacheDataSource.get()
            ?.firstOrNull { it.id == sourceProductId }?.value
    }

    open fun getPaymentTarget(): String? {
        return autofillCacheDataSource.get()
            ?.firstOrNull { it.id == targetProductId }?.value
    }

    override fun doOnExit() {
        runCatching {
            autofillCacheDataSource.clear()
        }
    }

    protected abstract val amountId: String
    protected abstract val currencyId: String
    protected abstract val sourceProductId: String
    protected abstract val targetProductId: String

    companion object {
        const val FIRST_FIELD_ID = "FIRST_FIELD_ID"
        const val LOG_TAG = "AUTOFILL"
    }
}