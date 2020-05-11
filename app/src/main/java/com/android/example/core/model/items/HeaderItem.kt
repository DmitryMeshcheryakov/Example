package com.android.example.core.model.items

import com.android.example.core.model.BaseModel

class HeaderItem(
    id: String = ID,
    var title: String = ""
) : BaseModel(id) {
    override var viewType: Int = VIEW_TYPE

    companion object {
        const val VIEW_TYPE = -998
        const val ID = "HEAD"
    }
}