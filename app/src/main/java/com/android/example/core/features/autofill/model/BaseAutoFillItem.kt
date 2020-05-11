package com.android.example.core.features.autofill.model

import com.android.example.core.model.BaseModel

open class BaseAutoFillItem(
    override var id: String,
    open var step: Int? = null,
    open var value: String? = null
) : BaseModel(id)