package com.android.example.core.features.fields.data

import android.content.Context
import com.android.example.core.features.fields.FieldErrorType

class FieldFileViewer(
    id: String,
    value: ByteArray?,
    var format: String,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    visible: Boolean = true,
    sortIndex: Int = 0
) : BaseField<ByteArray>(
    id,
    value,
    title,
    hint,
    false,
    true,
    visible,
    sortIndex,
    false
) {

    override var viewType: Int = VIEW_TYPE

    override var editable: Boolean = false

    override var optional: Boolean = true

    override var mustBeChanged: Boolean = false

    override fun getError(type: FieldErrorType, context: Context?): String? {
        return null
    }

    override fun getErrorType(): FieldErrorType {
        return FieldErrorType.NONE
    }

    override fun getStringValue(): String? {
        return null
    }

    override fun setStringValue(str: String?) {

    }

    companion object {
        const val VIEW_TYPE = 5497
    }
}