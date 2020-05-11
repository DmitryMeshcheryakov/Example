package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TimePicker
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.android.example.R
import com.android.example.core.ui.dialogs.BaseDialog
import java.util.*

open class DialogTime: BaseDialog() {

    companion object {
        const val TAG = "DialogTime"

        fun builder(): Builder {
            return DialogTime().Builder()
        }
    }


    override var layoutId: Int = R.layout.dlg_time

    protected var selectedTime: Date? = null
    protected var is24HourDay = true
    protected var onPositiveClickListener : ((from: Date) -> Unit)? = null

    override fun createView(context: Context): View? {
        val view = super.createView(context)
        val timePicker = view?.findViewById<TimePicker?>(R.id.timePicker)

        val calendar = Calendar.getInstance()
        calendar.time = selectedTime ?: Date()

        timePicker?.setIs24HourView(is24HourDay)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker?.hour = calendar.get(Calendar.MINUTE)
            timePicker?.minute = calendar.get(Calendar.MINUTE)
        } else {
            timePicker?.currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            timePicker?.currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        }

        timePicker?.setOnTimeChangedListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
        }

        bindPositiveButton(view) { onPositiveClickListener?.invoke(calendar.time) }
        return view
    }

    override fun createDialog(context: Context, view: View): Dialog {
        val dialogBuilder = themeId?.let { AlertDialog.Builder(context, it) } ?: AlertDialog.Builder(context)
        dialogBuilder.setView(view)
        return dialogBuilder.create()
    }

    inner class Builder : BaseDialog.Builder() {

        fun setSelectedTime(date : Date?) : Builder {
            this@DialogTime.selectedTime = date
            return this
        }

        fun set24HourDay(is24HourDay: Boolean?) : Builder {
            this@DialogTime.is24HourDay = is24HourDay ?: true
            return this
        }

        fun setSelectedTime(hour: Int, minute: Int) : Builder {
            val calendar = Calendar.getInstance()
            calendar.time = selectedTime ?: Date()

            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            return setSelectedTime(calendar.time)
        }

        fun setPositiveClickListener(listener : (Date) -> Unit) : Builder {
            this@DialogTime.onPositiveClickListener = listener
            return this
        }

        override fun setTheme(resId: Int?): Builder {
            super.setTheme(resId)
            return this
        }

        override fun setImage(img: Any?): Builder {
            super.setImage(img)
            return this
        }
        override fun setNegativeClickListener(listener: () -> Unit): Builder {
            super.setNegativeClickListener(listener)
            return this
        }

        override fun setNeutralClickListener(listener: () -> Unit): Builder {
            super.setNeutralClickListener(listener)
            return this
        }

        override fun showPositiveButton(show: Boolean): Builder {
            super.showPositiveButton(show)
            return this
        }

        override fun showNegativeButton(show: Boolean): Builder {
            super.showNegativeButton(show)
            return this
        }

        override fun showNeutralButton(show: Boolean): Builder {
            super.showNeutralButton(show)
            return this
        }

        override fun setOnDismissListener(listener: () -> Unit): Builder {
            super.setOnDismissListener(listener)
            return this
        }

        override fun setTitle(@StringRes resId: Int?): Builder {
            super.setTitle(resId)
            return this
        }

        override fun setTitle(text: CharSequence?): Builder {
            super.setTitle(text)
            return this
        }

        override fun setDescription(@StringRes resId: Int?): Builder {
            super.setDescription(resId)
            return this
        }

        override fun setDescription(text: CharSequence?): Builder {
            super.setDescription(text)
            return this
        }

        override fun setPositiveButton(@StringRes resId: Int?): Builder {
            super.setPositiveButton(resId)
            return this
        }

        override fun setPositiveButton(text: String?): Builder {
            super.setPositiveButton(text)
            return this
        }

        override fun setNegativeButton(@StringRes resId: Int?): Builder {
            super.setNegativeButton(resId)
            return this
        }

        override fun setNegativeButton(text: String?): Builder {
            super.setNegativeButton(text)
            return this
        }

        override fun setNeutralButton(@StringRes resId: Int?): Builder {
            super.setNeutralButton(resId)
            return this
        }

        override fun setNeutralButton(text: String?): Builder {
            super.setNeutralButton(text)
            return this
        }

        override fun setCancelable(cancelable: Boolean?): Builder {
            super.setCancelable(cancelable)
            return this
        }

        override fun addCustomViewParams(listener: ((view: View?, dialog: Dialog?) -> Unit)?): Builder {
            super.addCustomViewParams(listener)
            return this
        }

        /**
         * Layout requirements:
         * dialog image (ImageView) = @id/imageIv
         */
        override fun setLayoutId(@LayoutRes resId: Int): Builder {
            super.setLayoutId(resId)
            return this
        }
    }
}