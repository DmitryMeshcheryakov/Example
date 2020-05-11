package com.android.example.core.features.fields.data

import android.content.Context
import com.android.example.R
import com.android.example.core.features.fields.FieldErrorType
import com.android.example.core.utils.*

open class FieldDouble(
    id: String,
    value: Double?,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    editable: Boolean = true,
    optional: Boolean = true,
    visible: Boolean = true,
    sortIndex: Int = 0,
    mustBeChanged: Boolean = false,
    open var maxValue: Double? = null,
    open var minValue: Double? = null
) : BaseField<Double>(
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
        return value?.formatDigits
    }

    override fun setStringValue(str: String?) {
        value = str?.toDoubleOrNull()
    }

    override fun getErrorType(): FieldErrorType {
        val str = getStringValue()
        return when {
            str.isNullOrEmpty() -> if (optional) FieldErrorType.NONE else FieldErrorType.EMPTY
            !str.isValidMinValue(minValue) -> FieldErrorType.MIN_VAL
            !str.isValidMaxValue(maxValue) -> FieldErrorType.MAX_VAL
            else -> super.getErrorType()
        }
    }

    override fun getError(type: FieldErrorType, context: Context?): String? {
        return when (type) {
            FieldErrorType.MIN_VAL -> context?.getString(
                R.string.error_min_value,
                minValue?.formatDigits
            )
            FieldErrorType.MAX_VAL -> context?.getString(
                R.string.error_max_value,
                maxValue?.formatDigits
            )
            else -> super.getError(type, context)
        }
    }

    override var viewType: Int =
        VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 499
    }
}