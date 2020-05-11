package com.android.example.core.features.fields.data

class FieldStringPassword(
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
    regex: String? = null
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
    null
) {

    override var viewType: Int =
        VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 7899
    }
}