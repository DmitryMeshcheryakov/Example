package com.android.example.core.ui.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.android.example.R
import com.android.example.core.model.items.EmptyItem
import com.android.example.core.ui.listeners.BaseItemClickListener
import com.android.example.core.utils.loadImage

class EmptyViewHolder(view: View, clickListener: BaseItemClickListener? = null)
    : BaseViewHolder<EmptyItem, BaseItemClickListener>(view, clickListener) {

    var titleTv: AppCompatTextView? = itemView.findViewById(R.id.titleTv)
    var descriptionTv: AppCompatTextView? = itemView.findViewById(R.id.descriptionTv)
    var imageIv: AppCompatImageView? = itemView.findViewById(R.id.imageIv)

    override fun bind(item: EmptyItem) {
        super.bind(item)
        titleTv?.text = item.title
        descriptionTv?.text = item.description.orEmpty()
        imageIv?.loadImage( item.image)
        imageIv?.visibility = if (item.image == null) View.GONE else View.VISIBLE
        descriptionTv?.visibility = if (item.description.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}