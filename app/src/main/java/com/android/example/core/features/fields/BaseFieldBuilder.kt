package com.android.example.core.features.fields

import com.android.example.core.model.items.DlgItem
import com.android.example.core.features.fields.data.BaseField
import com.android.example.core.features.fields.data.Expression
import com.android.example.core.features.fields.data.FieldBoolean
import com.android.example.core.features.fields.data.FieldDate
import com.android.example.core.features.fields.data.FieldDouble
import com.android.example.core.features.fields.data.FieldLong
import com.android.example.core.features.fields.data.FieldSelector
import com.android.example.core.features.fields.data.FieldString
import com.android.example.core.features.fields.data.FieldStringArea
import com.android.example.core.features.fields.data.FieldStringDictionaries
import com.android.example.core.features.fields.data.FieldStringDigits
import com.android.example.core.features.fields.data.FieldStringDigitsPassword
import com.android.example.core.features.fields.data.FieldStringDigitsPhone
import com.android.example.core.features.fields.data.FieldStringHint
import com.android.example.core.features.fields.data.FieldStringPassword
import com.android.example.core.features.fields.data.Lookup
import com.android.example.core.utils.DateUtils
import java.util.*

/**
 * String
 * Long
 * Double
 * Date
 * Boolean
 */
open class BaseFieldBuilder(protected var id: String) {

    protected var title: CharSequence? = null
    protected var hint: CharSequence? = null
    protected var editable: Boolean = true
    protected var optional: Boolean = true
    protected var visible: Boolean = true
    protected var sortIndex: Int = 0
    protected var mustBeChanged: Boolean = false
    protected var data: Any? = null
    protected var externalError: String? = null
    protected var externalItem: BaseField<*>? = null
    protected var showExternalError: ((item: BaseField<*>) -> Boolean)? = null

    open fun setData(data: Any?): BaseFieldBuilder {
        this.data = data
        return this
    }

    open fun setTitle(title: CharSequence?): BaseFieldBuilder {
        this.title = title
        return this
    }

    open fun setHint(hint: CharSequence?): BaseFieldBuilder {
        this.hint = hint
        return this
    }

    open fun setEditable(editable: Boolean = true): BaseFieldBuilder {
        this.editable = editable
        return this
    }

    open fun setOptional(optional: Boolean = true): BaseFieldBuilder {
        this.optional = optional
        return this
    }

    open fun setVisible(visible: Boolean = true): BaseFieldBuilder {
        this.visible = visible
        return this
    }

    open fun setSortIndex(sortIndex: Int): BaseFieldBuilder {
        this.sortIndex = sortIndex
        return this
    }

    open fun setMustBeChanged(mustBeChanged: Boolean): BaseFieldBuilder {
        this.mustBeChanged = mustBeChanged
        return this
    }

    open fun setExternalError(externalError: String?): BaseFieldBuilder {
        this.externalError = externalError
        return this
    }

    open fun setExternalItem(externalItem: BaseField<*>?): BaseFieldBuilder {
        this.externalItem = externalItem
        return this
    }

    open fun setShowExternalError(showExternalError: ((item: BaseField<*>) -> Boolean)?): BaseFieldBuilder {
        this.showExternalError = showExternalError
        return this
    }

    protected fun fill(item: BaseField<*>) {
        item.title = title
        item.hint = hint
        item.editable = editable
        item.optional = optional
        item.visible = visible
        item.oldVisible = visible
        item.sortIndex = sortIndex
        item.mustBeChanged = mustBeChanged
        item.data = data
        item.externalError = externalError
        item.externalItem = externalItem
        item.showExternalError = showExternalError
    }

    open fun setValue(value: String?) = BaseFieldStringBuilder(value)

    open fun setValue(value: Boolean?) = BaseFieldBooleanBuilder(value)

    open fun setValue(value: Long?) = BaseFieldLongBuilder(value)

    open fun setValue(value: Double?) = BaseFieldDoubleBuilder(value)

    open fun setValue(value: Date?) = BaseFieldDateBuilder(value)

    open fun setValue(value: List<DlgItem>?) = BaseFieldSelectorBuilder(value)

    open inner class BaseFieldSelectorBuilder(var value: List<DlgItem>?) {

        protected var expressions: List<Expression<DlgItem>>? = null
        protected var enableEmptySelection: Boolean = false
        protected var enableMultipleSelection: Boolean = false
        protected var itemLayoutId: Int? = null

        open fun setExpressions(expressions: List<Expression<DlgItem>>?): BaseFieldSelectorBuilder {
            this.expressions = expressions
            return this
        }

        open fun enableEmptySelection(enable: Boolean?): BaseFieldSelectorBuilder {
            this.enableEmptySelection = enable ?: false
            return this
        }

        open fun enableMultipleSelection(enable: Boolean?): BaseFieldSelectorBuilder {
            this.enableMultipleSelection = enable ?: false
            return this
        }

        open fun setItemLayout(id: Int?): BaseFieldSelectorBuilder {
            this.itemLayoutId = id
            return this
        }

        open fun build(): BaseField<*> {
            return FieldSelector(
                id,
                value,
                enableEmptySelection = enableEmptySelection,
                enableMultipleSelection = enableMultipleSelection,
                itemLayoutId = itemLayoutId
            ).apply { fill(this) }
        }
    }

    open inner class BaseFieldStringBuilder(protected var value: String?) {

        protected var expressions: List<Expression<String>>? = null
        protected var maxLength: Int? = null
        protected var minLength: Int? = null
        protected var isPassword: Boolean = false
        protected var regex: String? = null
        protected var predefinedSymbols: String? = null
        protected var onlyDigits: Boolean = false
        protected var lines: Int? = null
        protected var lookups: List<Lookup<String>>? = null
        protected var showRefreshButton = false
        protected var isPhone: Boolean = false
        protected var disableHint: Boolean = false
        protected var isDictionary: Boolean = false
        protected var phonePrefix: String? = null

        protected fun fill(item: FieldString) {
            this@BaseFieldBuilder.fill(item)
            item.maxLength = maxLength
            item.minLength = minLength
            item.regex = regex
            item.lookups = lookups
            item.expressions = expressions
        }

        open fun setAsPhone(isPhone: Boolean = true): BaseFieldStringBuilder {
            if (isPhone) {
                onlyDigits = true
                this.isPhone = true
                isDictionary = false
            } else {
                this.isPhone = false
            }
            return this
        }

        open fun disableHint(disable: Boolean = true): BaseFieldStringBuilder {
            this.disableHint = disable
            return this
        }

        open fun setPhonePrefix(prefix: String?): BaseFieldStringBuilder {
            phonePrefix = prefix
            return this
        }

        open fun setAsDictionary(isDictionary: Boolean = true): BaseFieldStringBuilder {
            this.isDictionary = isDictionary
            return this
        }

        open fun setShowRefreshButton(showRefreshButton: Boolean = true): BaseFieldStringBuilder {
            this.showRefreshButton = showRefreshButton
            return this
        }

        open fun setExpressions(expressions: List<Expression<String>>?): BaseFieldStringBuilder {
            this.expressions = expressions
            return this
        }

        open fun setOnlyDigits(onlyDigits: Boolean = true): BaseFieldStringBuilder {
            this.onlyDigits = onlyDigits
            return this
        }

        open fun setPredefinedSymbols(symbols: String?): BaseFieldStringBuilder {
            this.predefinedSymbols = symbols
            return this
        }

        open fun setLines(lines: Int?): BaseFieldStringBuilder {
            this.lines = lines
            return this
        }

        open fun setMaxLength(maxLength: Int?): BaseFieldStringBuilder {
            this.maxLength = maxLength
            return this
        }

        open fun setMinLength(minLength: Int?): BaseFieldStringBuilder {
            this.minLength = minLength
            return this
        }

        open fun setIsPassword(isPassword: Boolean = true): BaseFieldStringBuilder {
            this.isPassword = isPassword
            return this
        }

        open fun setRegex(regex: String?): BaseFieldStringBuilder {
            this.regex = regex
            return this
        }

        open fun setLookups(lookups: List<Lookup<String>>?): BaseFieldStringBuilder {
            this.lookups = lookups
            return this
        }

        open fun build(): BaseField<*> {
            val phone = isPhone ||
                    title?.toString().orEmpty().toLowerCase(Locale.getDefault()).contains("телефон") ||
                    title?.toString().orEmpty().toLowerCase(Locale.getDefault()).contains("phone")

            return when {
                isDictionary && !lookups.isNullOrEmpty() -> {
                    val v = lookups?.firstOrNull { it.value == value }
                    FieldStringDictionaries(id, v, lookups = lookups)
                }
                onlyDigits && isPassword -> FieldStringDigitsPassword(
                    id,
                    value,
                    showRefreshBtn = showRefreshButton
                )
                onlyDigits && phone -> {
                    regex = null
                    FieldStringDigitsPhone(
                        id,
                        value,
                        phonePrefix = phonePrefix
                    )
                }
                onlyDigits -> FieldStringDigits(id, value)
                isPassword -> FieldStringPassword(id, value)
                lines ?: 0 > 1 -> FieldStringArea(
                    id,
                    value,
                    lines = lines ?: 1
                )
                !editable && optional && !disableHint -> FieldStringHint(id, value)
                else -> FieldString(
                    id,
                    value,
                    predefinedSymbols = predefinedSymbols
                )
            }.apply {
                if (this is FieldString) this@BaseFieldStringBuilder.fill(this)
                else this@BaseFieldBuilder.fill(this)
            }
        }
    }

    open inner class BaseFieldLongBuilder(protected var value: Long?) {

        protected var expressions: List<Expression<Long>>? = null
        protected var maxValue: Long? = null
        protected var minValue: Long? = null
        protected var isPassword: Boolean = false

        protected fun fill(item: FieldLong) {
            this@BaseFieldBuilder.fill(item)
            item.maxValue = maxValue
            item.minValue = minValue
            item.expressions = expressions
        }

        open fun setExpressions(expressions: List<Expression<Long>>?): BaseFieldLongBuilder {
            this.expressions = expressions
            return this
        }

        open fun setMaxValue(maxValue: Long?): BaseFieldLongBuilder {
            this.maxValue = maxValue
            return this
        }

        open fun setMinValue(minValue: Long?): BaseFieldLongBuilder {
            this.minValue = minValue
            return this
        }

        open fun setIsPassword(isPassword: Boolean = true): BaseFieldLongBuilder {
            this.isPassword = isPassword
            return this
        }

        open fun build(): BaseField<*> {
            return when {
                else -> FieldLong(
                    id,
                    value
                ).apply { this@BaseFieldLongBuilder.fill(this) }
            }
        }
    }

    open inner class BaseFieldDoubleBuilder(protected var value: Double?) {

        protected var expressions: List<Expression<Double>>? = null
        protected var maxValue: Double? = null
        protected var minValue: Double? = null
        protected var commissionAmount: Double? = null
        protected var commissionPercent: Double? = null
        protected var currency: Currency? = null
        protected var currencies: Set<Currency>? = null

        protected fun fill(item: FieldDouble) {
            this@BaseFieldBuilder.fill(item)
            item.maxValue = maxValue
            item.minValue = minValue
            item.expressions = expressions
        }

        open fun setCommissionAmount(commission: Double?): BaseFieldDoubleBuilder {
            this.commissionAmount = commission
            return this
        }

        open fun setCommissionPercent(commission: Double?): BaseFieldDoubleBuilder {
            this.commissionPercent = commission
            return this
        }

        open fun setCurrency(currency: Currency?): BaseFieldDoubleBuilder {
            this.currency = currency
            return this
        }

        open fun setAvailableCurrencies(currencies: Set<Currency>): BaseFieldDoubleBuilder {
            this.currencies = currencies
            return this
        }

        open fun setExpressions(expressions: List<Expression<Double>>?): BaseFieldDoubleBuilder {
            this.expressions = expressions
            return this
        }

        open fun setMaxValue(maxValue: Double?): BaseFieldDoubleBuilder {
            this.maxValue = maxValue
            return this
        }

        open fun setMinValue(minValue: Double?): BaseFieldDoubleBuilder {
            this.minValue = minValue
            return this
        }

        open fun build(): BaseField<*> {
            return FieldDouble(
                id,
                value
            )
            .apply { this@BaseFieldDoubleBuilder.fill(this) }
        }
    }

    open inner class BaseFieldDateBuilder(protected var value: Date?) {

        protected var expressions: List<Expression<Date>>? = null
        protected var maxValue: Date? = null
        protected var minValue: Date? = null
        protected var pattern: String? = null

        protected fun fill(item: FieldDate) {
            this@BaseFieldBuilder.fill(item)
            item.pattern = pattern ?: DateUtils.PATTERN_FULL_DATE
            item.maxValue = maxValue
            item.minValue = minValue
            item.expressions = expressions
        }

        open fun setExpressions(expressions: List<Expression<Date>>?): BaseFieldDateBuilder {
            this.expressions = expressions
            return this
        }

        open fun setMaxValue(maxValue: Date?): BaseFieldDateBuilder {
            this.maxValue = maxValue
            return this
        }

        open fun setMinValue(minValue: Date?): BaseFieldDateBuilder {
            this.minValue = minValue
            return this
        }

        open fun setPattern(pattern: String?): BaseFieldDateBuilder {
            this.pattern = pattern
            return this
        }

        open fun build(): BaseField<*> {
            val p = pattern ?: throw IllegalArgumentException("pattern must be set")

            return when {
                else -> FieldDate(
                    id,
                    value,
                    p
                )
            }.apply { this@BaseFieldDateBuilder.fill(this) }
        }
    }

    open inner class BaseFieldBooleanBuilder(protected var value: Boolean?) {

        protected var expressions: List<Expression<Boolean>>? = null

        protected fun fill(item: FieldBoolean) {
            this@BaseFieldBuilder.fill(item)
            item.expressions = expressions
        }

        open fun setExpressions(expressions: List<Expression<Boolean>>?): BaseFieldBooleanBuilder {
            this.expressions = expressions
            return this
        }

        open fun build(): BaseField<*> {
            return when {
                else -> FieldBoolean(
                    id,
                    value
                )
            }.apply { this@BaseFieldBooleanBuilder.fill(this) }
        }
    }
}

