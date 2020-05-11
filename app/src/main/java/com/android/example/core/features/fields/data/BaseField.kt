package com.android.example.core.features.fields.data

import android.content.Context
import com.android.example.R
import com.android.example.core.features.fields.FieldErrorType
import com.android.example.core.model.BaseModel

abstract class BaseField<V>(
    override var id: String,
    value: V?,
    open var title: CharSequence?,
    open var hint: CharSequence?,
    open var editable: Boolean,
    open var optional: Boolean,
    visible: Boolean,
    open var sortIndex: Int,
    open var mustBeChanged: Boolean = false
) : BaseModel(id) {

    open var data: Any? = null
    open var showErrors: Boolean = true
    open var externalError: String? = null
    open var externalItem: BaseField<*>? = null
    open var showExternalError: ((item: BaseField<*>) -> Boolean)? =
        { !externalError.isNullOrEmpty() }
    open var expressions: List<Expression<V>>? = null

    open var oldVisible: Boolean = visible
    open var visible: Boolean = visible

    open var oldValue: V? = value
    open var value: V? = value
        set(value) { if (editable) field = value }
        get() = if (editable) field else oldValue


    open fun reset() {
        value = oldValue
    }

    open fun hasChanges(): Boolean = oldValue != value

    open fun getErrorType(): FieldErrorType {
        val str = getStringValue()
        return when {
            str.isNullOrEmpty() -> if (optional) FieldErrorType.NONE else FieldErrorType.EMPTY
            showExternalError?.invoke(this) == true -> FieldErrorType.EXTERNAL
            mustBeChanged && oldValue == value -> FieldErrorType.NOT_CHANGED
            else -> FieldErrorType.NONE
        }
    }

    open fun getError(type: FieldErrorType, context: Context?): String? {
        return when (type) {
            FieldErrorType.EXTERNAL -> externalError
            FieldErrorType.EMPTY -> context?.getString(R.string.error_empty_value)
            FieldErrorType.NOT_CHANGED -> context?.getString(R.string.error_value_not_changed)
            else -> null
        }
    }

    open fun needShowItems(): List<String> {
        return expressions?.firstOrNull { it.onValue == value }?.show.orEmpty()
    }

    open fun needHideItems(): List<String> {
        return expressions?.firstOrNull { it.onValue == value }?.hide.orEmpty()
    }

    abstract fun getStringValue(): String?

    abstract fun setStringValue(str: String?)
}

fun List<BaseModel>.getFieldFirstErrorPos(): Int? {
    val pos = indexOfFirst {
        val errorType = (it as? BaseField<*>?)?.getErrorType() ?: FieldErrorType.NONE
        errorType != FieldErrorType.NONE
    }
    return if (pos < 0) null
    else pos
}

fun List<BaseModel>.getFieldErrorsCount(): Int {
    return asSequence()
        .filterIsInstance<BaseField<*>>()
        .filterNot { it.getErrorType() == FieldErrorType.NONE }
        .filter { it.visible }
        .count()
}

fun List<BaseModel>.resetFields() {
    asSequence().filterIsInstance<BaseField<*>>()
        .forEach { it.reset() }
}

fun List<BaseModel>.applyExpression(item: BaseField<*>): List<String> {
    val needUpdateItems = mutableListOf<String>()
    if (!item.expressions.isNullOrEmpty()) {
        val hide = item.needHideItems()
        val show = item.needShowItems()
        asSequence().filterIsInstance<BaseField<*>>()
            .forEach {
                when {
                    hide.isEmpty() && show.isEmpty() && it.visible != it.oldVisible -> {
                        it.visible = it.oldVisible
                        needUpdateItems.add(it.id)
                    }
                    hide.contains(it.id) && it.visible -> {
                        it.visible = false
                        needUpdateItems.add(it.id)
                    }
                    show.contains(it.id) && !it.visible -> {
                        it.visible = true
                        needUpdateItems.add(it.id)
                    }
                }
            }
    }
    return needUpdateItems
}