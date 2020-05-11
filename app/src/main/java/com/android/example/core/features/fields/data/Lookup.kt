package com.android.example.core.features.fields.data

import com.android.example.core.model.BaseModel

open class Lookup<V>(
    override var id: String,
    open var value: V,
    open var title: String = value.toString(),
    open var image: Any? = null
): BaseModel(id)