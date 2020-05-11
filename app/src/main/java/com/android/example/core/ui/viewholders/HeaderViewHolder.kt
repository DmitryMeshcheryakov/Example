package com.android.example.core.ui.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.android.example.R
import com.android.example.core.model.items.HeaderItem
import com.android.example.core.ui.listeners.BaseItemClickListener

class HeaderViewHolder(view: View, clickListener: BaseItemClickListener? = null)
    : BaseViewHolder<HeaderItem, BaseItemClickListener>(view, clickListener) {

    var textView: AppCompatTextView? = view.findViewById(R.id.textView)

    override fun bind(item: HeaderItem) {
        super.bind(item)
        textView?.text = item.title
    }
}