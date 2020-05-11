package com.android.example.core.features.fields.data

import android.content.Context
import com.android.example.R
import com.android.example.core.features.fields.FieldErrorType
import com.android.example.core.utils.formatDigits
import com.android.example.core.utils.isValid
import com.android.example.core.utils.isValidMaxLength
import com.android.example.core.utils.isValidMinLength

open class FieldString(
    id: String,
    value: String?,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    editable: Boolean = true,
    optional: Boolean = true,
    visible: Boolean = true,
    sortIndex: Int = 0,
    mustBeChanged: Boolean = false,
    open var maxLength: Int? = null,
    open var minLength: Int? = null,
    open var regex: String? = null,
    open var predefinedSymbols: String? = null,
    open var lookups: List<Lookup<String>>? = null
) : BaseField<String>(
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
        return value
    }

    override fun setStringValue(str: String?) {
        value = str
    }

    override fun getErrorType(): FieldErrorType {
        val str = getStringValue()
        return when {
            str.isNullOrEmpty() -> if (optional) FieldErrorType.NONE else FieldErrorType.EMPTY
            !str.isValidMinLength(minLength) -> FieldErrorType.MIN_LEN
            !str.isValidMaxLength(maxLength) -> FieldErrorType.MAX_LEN
            !value.isNullOrEmpty() && !str.isValid(regex) -> FieldErrorType.REGEX
            else -> super.getErrorType()
        }
    }

    override fun getError(type: FieldErrorType, context: Context?): String? {
        return when (type) {
            FieldErrorType.MAX_LEN -> context?.getString(
                R.string.error_max_length,
                maxLength?.formatDigits
            )
            FieldErrorType.MIN_LEN -> context?.getString(
                R.string.error_min_length,
                minLength?.formatDigits
            )
            FieldErrorType.REGEX -> context?.getString(R.string.error_invalid_value)
            else -> super.getError(type, context)
        }
    }

    override var viewType: Int =
        VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 799
    }
}