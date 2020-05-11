package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.android.example.R
import com.android.example.core.model.DateRangeValidator
import com.android.example.core.ui.dialogs.BaseDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class MaterialDialogDate : BaseDialog() {

    companion object {
        const val TAG = "MaterialDialogDate"

        fun builder(): Builder {
            return MaterialDialogDate().Builder()
        }
    }

    override var layoutId: Int = R.layout.dlg_date

    protected var maxDate: Date? = null
    protected var minDate: Date? = null
    protected var selectedDate: Date? = null
    protected var onPositiveClickListener: ((Date) -> Unit)? = null
    protected var excludedDates: List<Long>? = null
    protected var excludeWeekends: Boolean = false

    override fun createView(context: Context): View? {
        throw IllegalStateException()
    }

    override fun createDialog(context: Context, view: View): Dialog {
        throw IllegalStateException()
    }

    inner class Builder : BaseDialog.Builder() {

        fun excludedDates(excludedDates: List<Date>? = null): Builder {
            this@MaterialDialogDate.excludedDates = excludedDates?.map { it.time }
            return this
        }

        fun excludeWeekends(excludeWeekends: Boolean = true): Builder {
            this@MaterialDialogDate.excludeWeekends = excludeWeekends
            return this
        }

        fun setMaxDate(date: Date?): Builder {
            this@MaterialDialogDate.maxDate = date
            return this
        }

        fun setMinDate(date: Date?): Builder {
            this@MaterialDialogDate.minDate = date
            return this
        }

        fun setSelectedDate(date: Date?): Builder {
            this@MaterialDialogDate.selectedDate = date
            return this
        }

        fun setPositiveClickListener(listener: (Date) -> Unit): Builder {
            this@MaterialDialogDate.onPositiveClickListener = listener
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

        override fun show(context: FragmentActivity?) {
            val builder = MaterialDatePicker.Builder.datePicker()

            builder.setTitleText(titleResId ?: R.string.select_date)
            titleString?.let { builder.setTitleText(it) }
            selectedDate?.let { builder.setSelection(it.time) }

            val calendarConstraints = CalendarConstraints.Builder()

            maxDate?.let { calendarConstraints.setEnd(it.time) }
            minDate?.let { calendarConstraints.setStart(it.time) }
            calendarConstraints.setValidator(
                DateRangeValidator(
                minDate?.time,
                maxDate?.time,
                excludedDates,
                excludeWeekends
            )
            )

            builder.setCalendarConstraints(calendarConstraints.build())
            themeId?.let { builder.setTheme(it) }

            val dlg = builder.build()

            dlg.addOnDismissListener { onDismissListener?.invoke() }
            dlg.addOnPositiveButtonClickListener { onPositiveClickListener?.invoke(Date(it)) }
            dlg.addOnCancelListener { onNeutralClickListener?.invoke() }
            dlg.addOnNegativeButtonClickListener { onNegativeClickListener?.invoke() }
            dlg.isCancelable = isCancelable

            context?.supportFragmentManager?.let { dlg.show(it, TAG) }
        }
    }
}