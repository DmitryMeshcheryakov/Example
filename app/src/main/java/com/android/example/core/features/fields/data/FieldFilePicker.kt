package com.android.example.core.features.fields.data

import android.content.Context
import com.android.example.R
import com.android.example.core.features.fields.FieldErrorType
import com.android.example.core.utils.formatDigits
import java.io.File

class FieldFilePicker(
    id: String,
    value: List<File>? = null,
    title: CharSequence? = null,
    hint: CharSequence? = null,
    editable: Boolean = true,
    optional: Boolean = true,
    visible: Boolean = true,
    sortIndex: Int = 0,
    mustBeChanged: Boolean = false,
    var maxSize: Long? = null,
    var minSize: Long? = null,
    var minCount: Int? = null,
    var maxCount: Int? = null
) : BaseField<List<File>>(
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

    override fun getError(type: FieldErrorType, context: Context?): String? {
        return when (type) {
            FieldErrorType.MIN_VAL -> context?.getString(
                R.string.error_min_file_size
            )
            FieldErrorType.MAX_VAL -> context?.getString(
                R.string.error_max_file_size
            )
            FieldErrorType.MIN_LEN -> context?.getString(
                R.string.error_min_file_count,
                minCount?.formatDigits
            )
            FieldErrorType.MAX_LEN -> context?.getString(
                R.string.error_max_file_count,
                maxCount?.formatDigits
            )
            else -> super.getError(type, context)
        }
    }

    override fun getErrorType(): FieldErrorType {
        val files = value.orEmpty()
        return when {
            files.isEmpty() && !optional -> FieldErrorType.EMPTY
            files.size > maxCount ?: Int.MAX_VALUE -> FieldErrorType.MAX_LEN
            files.size < minCount ?: Int.MIN_VALUE -> FieldErrorType.MAX_LEN
            files.firstOrNull { it.length() > maxSize ?: Long.MAX_VALUE } != null -> FieldErrorType.MAX_VAL
            files.firstOrNull { it.length() < minSize ?: Long.MIN_VALUE } != null -> FieldErrorType.MIN_VAL
            files.firstOrNull { it.isDirectory } != null -> FieldErrorType.REGEX
            else -> super.getErrorType()
        }
    }

    override fun getStringValue(): String? {
        return null
    }

    override fun setStringValue(str: String?) {

    }

    companion object {
        const val VIEW_TYPE = 3094
    }
}