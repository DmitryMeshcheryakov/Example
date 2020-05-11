package com.android.example.core.common

import android.text.InputFilter
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


interface BaseEditText {

    fun afterTextChange(listener: ((text: String) -> Unit)?)

    fun setOnButtonClick(listener: (() -> Unit)?)

    fun setText(@StringRes resId: Int)

    fun setText(text: CharSequence?)

    fun setHint(@StringRes resId: Int)

    fun setHint(text: CharSequence?)

    fun setButtonText(@StringRes resId: Int)

    fun setButtonText(text: CharSequence?)

    fun setError(@StringRes resId: Int)

    fun setError(message: CharSequence?)

    fun setButtonImage(@DrawableRes imageRes: Int)

    fun setLines(lines: Int?)

    fun setInputType(inputType: Int)

    fun setImeAction(ime: Int)

    fun setButton(button: Int)

    fun setMaxLength(max: Int?)

    fun setEditable(editable: Boolean)

    fun getText(): String

    fun addInputFilter(filter: InputFilter)

    companion object {
        const val INPUT_TYPE_PLAIN = 0
        const val INPUT_TYPE_PHONE = 1
        const val INPUT_TYPE_AMOUNT = 2
        const val INPUT_TYPE_DOUBLE = 3
        const val INPUT_TYPE_INT = 4
        const val INPUT_TYPE_PASSWORD = 5
        const val INPUT_TYPE_PASSWORD_NUMERIC = 6

        const val BUTTON_NONE = 0
        const val BUTTON_TEXT = 1
        const val BUTTON_IMAGE = 2
        const val BUTTON_DROPDOWN = 3
    }
}