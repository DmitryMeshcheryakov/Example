package com.android.example.core.model

import androidx.annotation.DrawableRes
import com.android.example.core.model.BaseModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

open class BaseMapMarker (
    id: String,
    open var text: String = "",
    @DrawableRes open var imageRes: Int,
    open var coordinates: Pair<Double, Double>
) : ClusterItem, BaseModel(id) {

    override fun getSnippet(): String = text

    override fun getTitle(): String = text

    override fun getPosition(): LatLng = LatLng(coordinates.first, coordinates.second)
}