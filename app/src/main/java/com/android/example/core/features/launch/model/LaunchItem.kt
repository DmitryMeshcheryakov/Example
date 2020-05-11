package com.android.example.core.features.launch.model

import com.android.example.core.model.BaseModel

open class LaunchItem<D>(
    id: String,
    open var data: D,
    open var onlyAuthZone: Boolean = false
) : BaseModel(id) {

    companion object {

        const val ID_QR = "QR"

        fun createForQr(data: String): LaunchItem<String> {
            return LaunchItem(
                ID_QR,
                data,
                true
            )
        }

        fun createForShortcut(type: String, onlyAuthZone: Boolean): LaunchItem<String> {
            return LaunchItem(
                type,
                type,
                onlyAuthZone
            )
        }

        fun <D> createForNotification(type: String, data: D, onlyAuthZone: Boolean): LaunchItem<D> {
            return LaunchItem(
                type,
                data,
                onlyAuthZone
            )
        }
    }
}