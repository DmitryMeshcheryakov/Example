package com.android.example.core.features.fields.data

import com.android.example.core.utils.digits

open class FieldStringDigits(
    id: String,
    value: String?,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    editable: Boolean = true,
    optional: Boolean = true,
    visible: Boolean = true,
    sortIndex: Int = 0,
    mustBeChanged: Boolean = false,
    maxLength: Int? = null,
    minLength: Int? = null,
    regex: String? = null,
    lookups: List<Lookup<String>>? = null
) : FieldString(
    id,
    value,
    title,
    hint,
    editable,
    optional,
    visible,
    sortIndex,
    mustBeChanged,
    maxLength,
    minLength,
    regex,
    null,
    lookups
) {

    override fun setStringValue(str: String?) {
        super.setStringValue(str.digits)
    }

    override fun getStringValue(): String? {
        return super.getStringValue().digits
    }

    override var viewType: Int =
        VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 32984
    }
}