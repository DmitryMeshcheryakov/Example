package com.android.example.core.model.items

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

open class DlgItem(
    var id: String,
    var titleId: Int? = null,
    var title: String? = null,
    var descriptionId: Int? = null,
    var description: String? = null,
    var checked: Boolean = false,
    var checkable: Boolean = false,
    var enabled: Boolean = true,
    var image: Any? = null,
    var data: Any? = null
) {

    companion object {
        fun builder(id: String) = DlgItem(id).Builder()
    }

    inner class Builder {

        fun setTitle(text: String?) : Builder {
            title = text
            return this
        }

        fun setTitle(@StringRes resId: Int) : Builder {
            titleId = resId
            return this
        }

        fun setDescription(text: String?) : Builder {
            description = text
            return this
        }

        fun setDescription(@StringRes resId: Int) : Builder {
            descriptionId = resId
            return this
        }

        fun setCheckable(isCheckable: Boolean?) : Builder {
            checkable = isCheckable ?: false
            return this
        }

        fun setChecked(isChecked: Boolean?) : Builder {
            checked = isChecked ?: false
            return this
        }

        fun setEnabled(isEnabled: Boolean?) : Builder {
            enabled = isEnabled ?: false
            return this
        }

        fun setImageRes(@DrawableRes resId: Int?) : Builder {
            this@DlgItem.image = resId
            return this
        }

        fun setImage(image: Any?) : Builder {
            this@DlgItem.image = image
            return this
        }

        fun setData(data: Any?): Builder {
            this@DlgItem.data = data
            return this
        }

        fun build() = this@DlgItem
    }
}
