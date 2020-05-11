package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.android.example.R
import java.util.*

open class DialogDate : BaseDialog() {

    companion object {
        const val TAG = "DialogDate"

        fun builder(): Builder {
            return DialogDate().Builder()
        }
    }

    override var layoutId: Int = R.layout.dlg_date

    protected var maxDate: Date? = null
    protected var minDate: Date? = null
    protected var selectedDate: Date? = null
    protected var onPositiveClickListener: ((Date) -> Unit)? = null

    override fun createView(context: Context): View? {
        val view = super.createView(context)
        val datePicker = view?.findViewById<DatePicker?>(R.id.datePicker)

        val calendar = Calendar.getInstance()
        calendar.time = selectedDate ?: Date()

        minDate?.let { datePicker?.minDate = it.time }
        maxDate?.let { datePicker?.maxDate = it.time }

        datePicker?.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
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

        fun setMaxDate(date: Date?): Builder {
            this@DialogDate.maxDate = date
            return this
        }

        fun setMinDate(date: Date?): Builder {
            this@DialogDate.minDate = date
            return this
        }

        fun setSelectedDate(date: Date?): Builder {
            this@DialogDate.selectedDate = date
            return this
        }

        fun setPositiveClickListener(listener: (Date) -> Unit): Builder {
            this@DialogDate.onPositiveClickListener = listener
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