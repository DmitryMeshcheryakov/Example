package com.android.example.core.features.fields.data

import android.content.Context
import com.android.example.core.features.fields.FieldErrorType

class FieldStringHint(
    id: String,
    value: String?,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    optional: Boolean = true,
    visible: Boolean = true,
    sortIndex: Int = 0
) : FieldString(
    id,
    value,
    title,
    hint,
    false,
    optional,
    visible,
    sortIndex,
    false,
    null,
    null,
    null,
    null,
    null
) {

    override var viewType: Int =
        VIEW_TYPE

    override fun getErrorType(): FieldErrorType {
        return FieldErrorType.NONE
    }

    override fun getError(type: FieldErrorType, context: Context?): String? {
        return null
    }

    companion object {
        const val VIEW_TYPE = 899
    }
}