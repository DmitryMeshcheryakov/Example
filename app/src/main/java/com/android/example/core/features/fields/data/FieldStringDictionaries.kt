package com.android.example.core.features.fields.data

import com.android.example.core.features.fields.FieldErrorType

class FieldStringDictionaries(
    id: String,
    value: Lookup<String>?,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    editable: Boolean = true,
    optional: Boolean = true,
    visible: Boolean = true,
    sortIndex: Int = 0,
    mustBeChanged: Boolean = false,
    var lookups: List<Lookup<String>>? = null
) : BaseField<Lookup<String>>(
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

    override fun getErrorType(): FieldErrorType {
        val str = getStringValue()
        return when {
            str.isNullOrEmpty() -> if (optional) FieldErrorType.NONE else FieldErrorType.EMPTY
            else -> FieldErrorType.NONE
        }
    }

    override fun getStringValue(): String? {
        return value?.value
    }

    override fun setStringValue(str: String?) {
        value = str?.let { (value ?: Lookup(it, it, it)).apply { value = it } }
    }

    override var viewType: Int =
        VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 25566
    }
}