package com.android.example.core.features.fields.data

import androidx.annotation.LayoutRes
import com.android.example.core.features.fields.FieldErrorType
import com.android.example.core.model.items.DlgItem

class FieldSelector(
    id: String,
    value: List<DlgItem>?,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    editable: Boolean = true,
    optional: Boolean = true,
    visible: Boolean = true,
    sortIndex: Int = 0,
    mustBeChanged: Boolean = false,
    var enableEmptySelection: Boolean = false,
    var enableMultipleSelection: Boolean = false,
    @LayoutRes var itemLayoutId: Int? = null
) : BaseField<List<DlgItem>>(
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

    override var viewType: Int = VIEW_TYPE

    override fun getStringValue(): String? {
        return null
    }

    override fun setStringValue(str: String?) {

    }

    override fun getErrorType(): FieldErrorType {
        return when {
            value?.filter { it.checked }.isNullOrEmpty() -> if (optional || enableEmptySelection) FieldErrorType.NONE else FieldErrorType.EMPTY
            showExternalError?.invoke(this) == true -> FieldErrorType.EXTERNAL
            mustBeChanged && oldValue == value -> FieldErrorType.NOT_CHANGED
            else -> FieldErrorType.NONE
        }
    }

    companion object {
        const val VIEW_TYPE = 5407
    }
}