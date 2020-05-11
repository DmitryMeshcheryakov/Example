package com.android.example.core.features.fields

import android.text.SpannableStringBuilder
import android.view.View
import com.android.example.R
import com.android.example.core.features.fields.data.BaseField
import com.android.example.core.ui.viewholders.BaseViewHolder

abstract class BaseFieldViewHolder<V, I : BaseField<V>, L : BaseFieldItemListener>(
    view: View,
    listener: L? = null
) : BaseViewHolder<I, L>(view, listener) {

    override fun bind(item: I) {
        super.bind(item)
        unregisterListeners()
        setVisibility(item.visible)
        setEditable(item.editable)
        val titleMarker = itemView.context.getString(if (item.optional) R.string.field_title_optional_marker else R.string.field_title_mandatory_marker)
        val title = SpannableStringBuilder()
        if (!item.title.isNullOrEmpty()) title.append(titleMarker)
        title.append(item.title)
        title.append(titleMarker)
        showTitle(title)
        showValue(item)
        registerListeners()
    }

    protected open fun onValueChange(newValue: V?) {
        item?.let { item ->
            item.value = newValue
            val error = item.getError(item.getErrorType(), itemView.context)
            showError(error)
            item.let { itemClickListener?.onValueChanged(it) }
        }
    }

    protected open fun setEditable(editable: Boolean) {
        itemView.isEnabled = editable
    }

    protected open fun registerListeners() {}

    protected open fun unregisterListeners() {}

    protected abstract fun showTitle(title: CharSequence?)

    protected abstract fun showValue(item: I)

    protected abstract fun showError(message: CharSequence?)
}