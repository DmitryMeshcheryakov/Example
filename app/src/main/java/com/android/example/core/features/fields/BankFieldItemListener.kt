package com.android.example.core.features.fields

import android.net.Uri

import com.android.example.core.ui.listeners.BaseItemClickListener
import com.android.example.core.features.fields.data.BaseField
import com.android.example.core.features.fields.data.FieldDate
import com.android.example.core.features.fields.data.FieldFilePicker
import com.android.example.core.features.fields.data.FieldString
import com.android.example.core.features.fields.data.Lookup
import java.io.File
import java.util.*

interface BaseFieldItemListener : BaseItemClickListener {

    fun onValueChanged(item: BaseField<*>)

    fun onRefreshClick(item: BaseField<*>)

    fun selectDate(item: FieldDate, listener: (date: Date?) -> Unit)

    fun selectPhoneNumber(item: FieldString, listener: (phone: String?) -> Unit)

    fun selectCurrency(items: Set<Currency>, listener: (currency: Currency) -> Unit)

    fun <V> selectLookup(
        items: List<Lookup<V>>,
        title: String? = null,
        currentValue: V? = null,
        listener: (value: V) -> Unit
    )

    fun showFilePicker(attr: FieldFilePicker, listener: (File) -> Unit)

    fun saveAndOpenFile(data: ByteArray, title: String, format: String)

    fun openFile(uri: Uri?)
}