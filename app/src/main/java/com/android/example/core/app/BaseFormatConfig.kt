package com.android.example.core.app

open class BaseFormatConfig {

    open val fileUnits: Array<String> = arrayOf("B", "kB", "MB", "GB", "TB")
    open val optionalFieldTitleMarkerStart: Boolean = false
    open val optionalFieldTitleMarkerEnd: Boolean = true

    open val phoneMaskSymbol: Char = 'X'
    open val phonePrefix: String = "+375 "
    open val phoneMaxLength: Int = 12
    open val phoneSeparators: Array<String> = arrayOf("(", ") ", "-")

    open val cardNumberSeparator: Char = ' '
    open val cardNumberMask: Char = '*'
    open val cardNumberMaskStart: Int = 6
    open val cardNumberMaskEnd: Int = 11

    open val nullCurrency: String = ""
    open val nullAmount: String = "..."
    open val nullPercent: String = "..."

    open val amountDecimalSeparator: Char = '.'
    open val amountGroupSeparator: Char = ' '
    open val amountGroupSize: Int = 3
    open val amountMinimumFractionDigits: Int? = 0
    open val amountMaximumFractionDigits: Int? = 2
    open val amountForceUsePrefix: Boolean = false

    open val percentDecimalSeparator: Char = '.'
    open val percentGroupSeparator: Char = ' '
    open val percentGroupSize: Int = 3
    open val percentMinimumFractionDigits: Int? = 0
    open val percentMaximumFractionDigits: Int? = 5
    open val percentForceUsePrefix: Boolean = false

    open val datePatternFullDateWithTimeWithSeconds = "dd.MM.yyyy, HH:mm:ss"
    open val datePatternFullDateWithTime = "dd.MM.yyyy, HH:mm"
    open val datePatternFullDate = "dd.MM.yyyy"
    open val datePatternTime = "HH:mm"
    open val datePatternExpiryDate = "MM/yy"
    open val removeZeroTime: Boolean = false

}