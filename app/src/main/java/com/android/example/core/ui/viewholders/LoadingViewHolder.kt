package com.android.example.core.ui.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.android.example.R
import com.android.example.core.model.items.LoadingItem
import com.android.example.core.ui.listeners.BaseItemClickListener

class LoadingViewHolder(view: View, clickListener: BaseItemClickListener? = null) :
    BaseViewHolder<LoadingItem, BaseItemClickListener>(view, clickListener) {

    var textView: AppCompatTextView? = view.findViewById(R.id.textView)

    override fun bind(item: LoadingItem) {
        super.bind(item)
        textView?.text = item.title
    }
}