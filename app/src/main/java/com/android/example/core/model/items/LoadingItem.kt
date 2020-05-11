package com.android.example.core.model.items

import com.android.example.core.model.BaseModel

class LoadingItem(
    var title: String = "",
    isLarge: Boolean = false
) : BaseModel(ID) {
    override var viewType: Int = if (isLarge) VIEW_TYPE_LARGE else VIEW_TYPE

    companion object {
        const val VIEW_TYPE = -9992
        const val VIEW_TYPE_LARGE = -9993
        const val ID = "LOADING"
    }
}