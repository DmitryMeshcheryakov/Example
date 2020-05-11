package com.android.example.core.utils

import android.os.Build
import android.text.Html
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object FormatUtils {

    fun getDigits(s: String?, ignoredChars: String = ""): String {
        val digits = StringBuilder()
        var i = 0
        while (i < s.orEmpty().length) {
            val current = s.orEmpty()[i]
            if (Character.isDigit(current) || ignoredChars.contains(current))
                digits.append(current)
            i++
        }
        return digits.toString()
    }

    fun getNumber(s: String?, allowDot: Boolean = true): String? {
        if (s.isNullOrEmpty() || s == "-")
            return null

        val digits = StringBuilder(if (s.startsWith("-")) "-" else "")
        var i = 0
        var flag = true
        while (i < s.length) {
            val current = s[i]

            when {
                allowDot && flag && current == '.' -> {
                    digits.append('.')
                    flag = false
                }
                allowDot && flag && current == ',' -> {
                    digits.append('.')
                    flag = false
                }
                Character.isDigit(current) -> {
                    digits.append(current)
                }
            }
            i++
        }

        if (digits.isEmpty())
            return null

        if (digits.toString() == ".")
            return "0.0"

        if (digits.startsWith("."))
            digits.insert(0, "0")

        return digits.toString()
    }

    fun formatDigits(
        value: Double?,
        nullValue: String = "",
        decimalSeparator: Char = '.',
        groupSeparator: Char = ' ',
        groupSize: Int = 500,
        minimumFractionDigits: Int? = null,
        maximumFractionDigits: Int? = null,
        forceUsePrefix: Boolean = false
    ): String {
        if (value == null || value.isNaN())
            return nullValue

        val pattern = "###,###.###"

        val unusualSymbols = DecimalFormatSymbols(Locale.getDefault())
        unusualSymbols.decimalSeparator = decimalSeparator
        unusualSymbols.groupingSeparator = groupSeparator
        unusualSymbols.naN = nullValue

        val decimalFormat = DecimalFormat(pattern, unusualSymbols)
        decimalFormat.groupingSize = groupSize
        decimalFormat.maximumFractionDigits = (maximumFractionDigits ?: 50)
        decimalFormat.minimumFractionDigits = (minimumFractionDigits ?: 0)
        decimalFormat.roundingMode = RoundingMode.CEILING

        var result = decimalFormat.format(value)

        if (forceUsePrefix && value > 0)
            result = "+$result"

        return result
    }

    fun formatDigits(
        value: Long?,
        nullValue: String = "",
        groupSeparator: Char = ' ',
        groupSize: Int = 500,
        forceUsePrefix: Boolean = false
    ): String {
        if (value == null)
            return nullValue

        val pattern = "###,###.###"

        val unusualSymbols = DecimalFormatSymbols(Locale.getDefault())
        unusualSymbols.groupingSeparator = groupSeparator
        unusualSymbols.naN = nullValue

        val decimalFormat = DecimalFormat(pattern, unusualSymbols)
        decimalFormat.groupingSize = groupSize
        decimalFormat.roundingMode = RoundingMode.DOWN

        var result = decimalFormat.format(value)

        if (forceUsePrefix && value > 0)
            result = "+$result"

        return result
    }


    /**
     * min 6
     * max 13
     *
     * 0                    // 1
     * 0-0                  // 2  :: 1
     * 0-00                 // 3  :: 1
     * 0-00-0               // 4  :: 1,3
     * 0-00-00              // 5  :: 1,3
     * 00-00-00             // 6  :: 2,4
     * 000-00-00            // 7  :: 3,5
     * (00)00-00-00         // 8  :: 4,6    :: 0,2
     * (00)000-00-00        // 9  :: 5,7    :: 0,2
     * (000)000-00-00       // 10 :: 6,8    :: 0,3
     * 0(000)000-00-00      // 11 :: 7,9    :: 1,4
     * 000(00)000-00-00     // 12 :: 8,10   :: 3,5
     * 0-000(00)000-00-00   // 13 :: 1,9,11 :: 4,6
     */

    fun formatPhoneNumber(
        phone: String?,
        prefix: String = "375",
        withMask: Boolean = false,
        maskSym: Char = '-',
        maxPhoneLength: Int = 9
    ): String {
        if (phone.orEmpty().length <= prefix.length) return prefix

        var maxLen = maxPhoneLength - prefix.digits.orEmpty().length
        if (maxLen < 0) maxLen = 0

        var s = getDigits(phone.orEmpty().replace(prefix, ""))
        if (s.length > maxLen) s = s.substring(0, maxLen)

        val builder = StringBuilder(prefix)

        val separator1Pos = when (s.length) {  // (
            8 -> listOf(0)
            9 -> listOf(0)
            10 -> listOf(0)
            11 -> listOf(1)
            12 -> listOf(3)
            13 -> listOf(4)
            else -> emptyList()
        }
        val separator2Pos = when (s.length) {  // )
            8 -> listOf(2)
            9 -> listOf(2)
            10 -> listOf(3)
            11 -> listOf(4)
            12 -> listOf(5)
            13 -> listOf(6)
            else -> emptyList()
        }
        val separator3Pos = when (s.length) {  // -
            2 -> listOf(1)
            3 -> listOf(1)
            4 -> listOf(1, 3)
            5 -> listOf(1, 3)
            6 -> listOf(2, 4)
            7 -> listOf(3, 5)
            8 -> listOf(4, 6)
            9 -> listOf(5, 7)
            10 -> listOf(6, 8)
            11 -> listOf(7, 9)
            12 -> listOf(8, 10)
            13 -> listOf(1, 9, 11)
            else -> emptyList()
        }

        val maskRangeStart = when {
            withMask && s.length in 8..13 -> s.length - 5
            else -> -1
        }

        val maskRangeEnd = when {
            withMask && s.length in 8..13 -> s.length - 3
            else -> -1
        }

        var i = 0
        s.forEach {
            if (separator1Pos.contains(i))
                builder.append(" ")

            if (separator2Pos.contains(i))
                builder.append(" ")

            if (separator3Pos.contains(i))
                builder.append(" ")

            val sym = if (i in maskRangeStart..maskRangeEnd) maskSym else it

            builder.append(sym)
            i++
        }
        return builder.toString()
    }
}

val Int?.formatDigits: String
    get() = FormatUtils.formatDigits(this?.toLong())

val Long?.formatDigits: String
    get() = FormatUtils.formatDigits(this)

val Double?.formatDigits: String
    get() = FormatUtils.formatDigits(this)

val String.unescape: String
    get() = try {
        StringEscapeUtils.unescapeHtml(this)
    } catch (e: Exception) {
        this
    }

val String.escape: String
    get() = try {
        StringEscapeUtils.escapeHtml(this)
    } catch (e: Exception) {
        this
    }

val String.fromHtml: CharSequence
    get() = kotlin.runCatching {
        var temp = this
        if (temp.contains("@{link=")) {
            temp = temp.replace("@{link=", "<a href=\"")
            temp = temp.replace("|title=", "\">")
            temp = temp.replace("}", "</a>")
            temp = temp.replace("\\", "")
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(temp.unescape, Html.FROM_HTML_MODE_LEGACY)
        else
            Html.fromHtml(temp.unescape)
    }.getOrNull() ?: this

val String.formatPhone: String
    get() = FormatUtils.formatPhoneNumber(this, withMask = false)

val String.formatMaskedPhone: String
    get() = FormatUtils.formatPhoneNumber(this, withMask = true)

val String?.digits: String?
    get() {
        val d = FormatUtils.getDigits(this)
        return if (d.isEmpty()) null else d
    }

val String.longFromServer: Long?
    get() = toLongOrNull() ?: FormatUtils.getNumber(this, false)?.toLongOrNull()

val String.doubleFromServer: Double?
    get() = toDoubleOrNull() ?: FormatUtils.getNumber(this, true)?.toDoubleOrNull()

val String.intFromServer: Int?
    get() = toIntOrNull() ?: FormatUtils.getNumber(this, false)?.toIntOrNull()

val String.boolFromServer: Boolean
    get() {
        if (this.toLowerCase().contains("1"))
            return true
        if (this.toLowerCase().contains("2"))
            return true
        if (this.toLowerCase().contains("tru"))
            return true
        if (this.toLowerCase().contains("y"))
            return true
        return false
    }