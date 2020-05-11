package com.android.example.core.features.fields.data

import android.content.Context
import com.android.example.R
import com.android.example.core.features.fields.FieldErrorType
import com.android.example.core.utils.formatDate
import com.android.example.core.utils.getDate
import com.android.example.core.utils.DateUtils
import java.util.*

open class FieldDate(
    id: String,
    value: Date?,
    open var pattern: String = DateUtils.PATTERN_FULL_DATE,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    editable: Boolean = true,
    optional: Boolean = true,
    visible: Boolean = true,
    sortIndex: Int = 0,
    mustBeChanged: Boolean = false,
    open var maxValue: Date? = null,
    open var minValue: Date? = null
) : BaseField<Date>(
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
        return value?.formatDate(pattern)
    }

    override fun setStringValue(str: String?) {
        value = str?.getDate(pattern)
    }

    override fun getErrorType(): FieldErrorType {
        val str = getStringValue()
        return when {
            str.isNullOrEmpty() -> if (optional) FieldErrorType.NONE else FieldErrorType.EMPTY
            (minValue?.time ?: Long.MIN_VALUE) > (value?.time ?: 0) -> FieldErrorType.MIN_VAL
            (maxValue?.time ?: Long.MAX_VALUE) < (value?.time ?: 0) -> FieldErrorType.MAX_VAL
            else -> super.getErrorType()
        }
    }

    override fun getError(type: FieldErrorType, context: Context?): String? {
        return when (type) {
            FieldErrorType.MIN_VAL -> context?.getString(
                R.string.error_min_value,
                minValue?.formatDate(pattern)
            )
            FieldErrorType.MAX_VAL -> context?.getString(
                R.string.error_max_value,
                maxValue?.formatDate(pattern)
            )
            else -> super.getError(type, context)
        }
    }

    override var viewType: Int =
        VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 399
    }
}