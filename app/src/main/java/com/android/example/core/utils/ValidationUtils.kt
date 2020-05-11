package com.android.example.core.utils

import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.android.example.core.common.BaseEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

object ValidationUtils {

    fun showError(fragment: Fragment?, field: String, message: String?) {
        runCatching {
            when (val v = fragment?.view?.findViewWithTag<View?>(field)) {
                is BaseEditText -> v.setError(message)
                is EditText -> v.error = message
                is TextInputLayout -> {
                    v.isErrorEnabled = !message.isNullOrEmpty()
                    v.error = message.orEmpty()
                }
            }
        }
    }

    fun isValidMinLength(value: String?, min: Int?): Boolean {
        return value.orEmpty().length >= min ?: Int.MIN_VALUE
    }

    fun isValidMaxLength(value: String?, max: Int?): Boolean {
        return value.orEmpty().length <= max ?: Int.MAX_VALUE
    }

    fun isValidMinValue(value: String?, min: Double?): Boolean {
        return (value?.doubleFromServer ?: 0.0) >= (min ?: Double.MIN_VALUE)
    }

    fun isValidMaxValue(value: String?, max: Double?): Boolean {
        return (value?.doubleFromServer ?: 0.0) <= (max ?: Double.MAX_VALUE)
    }

    fun isValidMinValue(value: String?, min: Long?): Boolean {
        return (value?.longFromServer ?: 0) >= (min ?: Long.MIN_VALUE)
    }

    fun isValidMaxValue(value: String?, max: Long?): Boolean {
        return (value?.longFromServer ?: 0) <= (max ?: Long.MAX_VALUE)
    }

    fun isValid(value: String?, pattern: Regex?): Boolean {
        return when {
            value.isNullOrEmpty() -> false
            pattern == null -> true
            else -> pattern.matches(value)
        }
    }
}

fun String?.isValidMinLength(min: Int?): Boolean {
    return ValidationUtils.isValidMinLength(this, min)
}

fun String?.isValidMaxLength(max: Int?): Boolean {
    return ValidationUtils.isValidMaxLength(this, max)
}

fun String?.isValidMinValue(min: Double?): Boolean {
    return ValidationUtils.isValidMinValue(this, min)
}

fun String?.isValidMaxValue(max: Double?): Boolean {
    return ValidationUtils.isValidMaxValue(this, max)
}

fun String?.isValidMinValue(min: Long?): Boolean {
    return ValidationUtils.isValidMinValue(this, min)
}

fun String?.isValidMaxValue(max: Long?): Boolean {
    return ValidationUtils.isValidMaxValue(this, max)
}

fun String?.isValid(pattern: String? = null): Boolean {
    return ValidationUtils.isValid(this, pattern?.toRegex())
}

val String?.isCardNumberValid: Boolean
    get() {
        val digits = this.orEmpty().map { Character.getNumericValue(it) }.toMutableList()
        for (i in (digits.size - 2) downTo 0 step 2) {
            var value = digits[i] * 2
            if (value > 9) {
                value = value % 10 + 1
            }
            digits[i] = value
        }
        return digits.sum() % 10 == 0 && this.orEmpty().length == 16
    }

val String?.isPassportNumberValid: Boolean
    get() {
        if (this.isNullOrEmpty())
            return false

        val regex = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d[A-Z]\\d\\d\\d[A-Z][A-Z]\\d", Pattern.CASE_INSENSITIVE).toRegex()
        return ValidationUtils.isValid(this, regex)
    }

val String?.isCardHolderValid: Boolean
    get() {
        if (this.isNullOrEmpty())
            return false

        val split = this.split(" ")
        val regex = Pattern.compile("^[A-Z.]*\\s?([A-Z\\-']+\\s)+[A-Z\\-']+\$", Pattern.CASE_INSENSITIVE).toRegex()

        if (split.size < 2)
            return false

        return ValidationUtils.isValid(this, regex)
    }

val String?.isCvvValid: Boolean
    get() {
        if (this.isNullOrEmpty())
            return false

        return ValidationUtils.isValid(this, "^([0-9]{3,4})\$".toRegex())
    }

val String?.isEmailValid: Boolean
    get() {
        if (this.isNullOrEmpty())
            return false

        return ValidationUtils.isValid(this, "[\\w-]+@([\\w-]+\\.)+[\\w-]+".toRegex())
    }

val String?.isUrlValid: Boolean
    get() {
        if (this.isNullOrEmpty())
            return false

        return ValidationUtils.isValid(this, "((mailto:|(news|(ht|f)tp(s?))://){1}\\S+)".toRegex())
    }
