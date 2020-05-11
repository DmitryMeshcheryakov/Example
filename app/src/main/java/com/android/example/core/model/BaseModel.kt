package com.android.example.core.model

import com.google.gson.annotations.SerializedName

open class BaseModel(
    @SerializedName("parent_id") open var id: String,
    @SerializedName("parent_titleText") open var titleText: String = ""
){

    open var viewType: Int = -1
}