package com.android.example.core.features.fields.data

import com.android.example.core.utils.boolFromServer

open class FieldBoolean(
    id: String,
    value: Boolean?,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    editable: Boolean = true,
    optional: Boolean = true,
    visible: Boolean = true,
    sortIndex: Int = 0,
    mustBeChanged: Boolean = false
) : BaseField<Boolean>(
    id,
    value,
    title,
    hint,
    editable,
    optional,
    visible,
    sortIndex,
    mustBeChanged
) {

    override fun getStringValue(): String? {
        return value?.let { if (it) "1" else "0" }
    }

    override fun setStringValue(str: String?) {
        value = str?.boolFromServer
    }

    override var viewType: Int =
        VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 299
    }
}