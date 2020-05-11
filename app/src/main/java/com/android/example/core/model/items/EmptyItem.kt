package com.android.example.core.model.items

import com.android.example.core.model.BaseModel

open class EmptyItem(
    var title: String,
    var description: String? = null,
    var image: Any? = null
) : BaseModel(ID) {
    override var viewType: Int = VIEW_TYPE

    companion object {
        const val VIEW_TYPE = -999
        const val ID = "EMPTY"
    }
}