package com.android.example.core.ui.viewholders

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.example.core.model.BaseModel
import com.android.example.core.ui.listeners.BaseItemClickListener

abstract class BaseViewHolder<I : BaseModel, L : BaseItemClickListener>(itemView: View, var itemClickListener: L? = null)
    : RecyclerView.ViewHolder(itemView) {

    protected var item: I? = null
    var isLast: Boolean = false
    var isPreLast: Boolean = false
    var isFirst: Boolean = false
    var isSecond: Boolean = false

    init {
        itemView.setOnClickListener { _ ->
            item?.let { itemClickListener?.onItemClick(it) }
        }
    }

    open fun bind(item : I) {
        this.item = item
    }

    open fun setVisibility(isVisible: Boolean) {
        val param = itemView.layoutParams as? RecyclerView.LayoutParams?
        param?.height = if (isVisible) LinearLayout.LayoutParams.WRAP_CONTENT else 0
        param?.width = if (isVisible) LinearLayout.LayoutParams.MATCH_PARENT else 0
        itemView.visibility = if (isVisible) View.VISIBLE else View.GONE
        param?.let { itemView.layoutParams = it }
    }
}