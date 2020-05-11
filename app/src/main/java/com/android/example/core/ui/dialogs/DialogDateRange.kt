package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.android.example.R
import com.android.example.core.utils.toEndOfDay
import com.android.example.core.utils.toStartOfDay
import java.util.*


open class DialogDateRange : BaseDialog() {

    companion object {
        const val TAG = "DialogDateRange"

        fun builder(): Builder {
            return DialogDateRange().Builder()
        }
    }

    override var layoutId: Int = R.layout.dlg_date_range

    protected var titleFrom: String? = null
    protected var titleFromRes: Int? = null

    protected var titleTo: String? = null
    protected var titleToRes: Int? = null

    protected var maxDate: Date? = null
    protected var minDate: Date? = null
    protected var startDate: Date? = null
    protected var endDate: Date? = null

    protected var onPositiveClickListener: ((startDate: Date, endDate: Date) -> Unit)? = null

    override fun createView(context: Context): View? {
        val view = super.createView(context)
        val datePickerFrom = view?.findViewById<DatePicker?>(R.id.datePickerFrom)
        val datePickerTo = view?.findViewById<DatePicker?>(R.id.datePickerTo)
        val fromBtn = view?.findViewById<TextView?>(R.id.fromBtn)
        val toBtn = view?.findViewById<TextView?>(R.id.toBtn)

        when {
            titleFrom != null -> fromBtn?.text = titleFrom
            titleFromRes != null -> fromBtn?.setText(titleFromRes!!)
            else -> fromBtn?.setText(R.string.date_range_from)
        }

        when {
            titleTo != null -> toBtn?.text = titleTo
            titleToRes != null -> toBtn?.setText(titleToRes!!)
            else -> toBtn?.setText(R.string.date_range_to)
        }


        val calendarFrom = Calendar.getInstance()
        calendarFrom.time = startDate ?: Date().toStartOfDay
        datePickerFrom?.init(
            calendarFrom.get(Calendar.YEAR),
            calendarFrom.get(Calendar.MONTH),
            calendarFrom.get(Calendar.DAY_OF_MONTH)
        ) { _, year, monthOfYear, dayOfMonth ->
            calendarFrom.set(Calendar.YEAR, year)
            calendarFrom.set(Calendar.MONTH, monthOfYear)
            calendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }


        val calendarTo = Calendar.getInstance()
        calendarTo.time = endDate ?: Date().toEndOfDay
        datePickerTo?.init(
            calendarTo.get(Calendar.YEAR),
            calendarTo.get(Calendar.MONTH),
            calendarTo.get(Calendar.DAY_OF_MONTH)
        ) { _, year, monthOfYear, dayOfMonth ->
            calendarTo.set(Calendar.YEAR, year)
            calendarTo.set(Calendar.MONTH, monthOfYear)
            calendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        val initFrom = {
            fromBtn?.isSelected = true
            toBtn?.isSelected = false

            datePickerFrom?.minDate = 0
            datePickerFrom?.maxDate = Long.MAX_VALUE
            datePickerTo?.minDate = 0
            datePickerTo?.maxDate = Long.MAX_VALUE

            datePickerFrom?.minDate = minDate?.time ?: 0
            datePickerFrom?.maxDate = calendarTo.timeInMillis

            datePickerFrom?.updateDate(calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH))

            datePickerFrom?.visibility = View.VISIBLE
            datePickerTo?.visibility = View.GONE
        }

        val initTo = {
            fromBtn?.isSelected = false
            toBtn?.isSelected = true

            datePickerFrom?.minDate = 0
            datePickerFrom?.maxDate = Long.MAX_VALUE
            datePickerTo?.minDate = 0
            datePickerTo?.maxDate = Long.MAX_VALUE

            datePickerTo?.minDate = calendarFrom.timeInMillis
            datePickerTo?.maxDate = maxDate?.time ?: Long.MAX_VALUE

            datePickerTo?.updateDate(calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH))

            datePickerFrom?.visibility = View.GONE
            datePickerTo?.visibility = View.VISIBLE
        }

        fromBtn?.setOnClickListener { initFrom.invoke() }
        toBtn?.setOnClickListener { initTo.invoke() }
        fromBtn?.callOnClick()

        bindPositiveButton(view) { onPositiveClickListener?.invoke(calendarFrom.time, calendarTo.time) }
        return view
    }

    override fun createDialog(context: Context, view: View): Dialog {
        val dialogBuilder = themeId?.let { AlertDialog.Builder(context, it) } ?: AlertDialog.Builder(context)
        dialogBuilder.setView(view)
        return dialogBuilder.create()
    }

    inner class Builder : BaseDialog.Builder() {

        fun setTitleFrom(@StringRes resId: Int): Builder {
            this@DialogDateRange.titleFromRes = resId
            return this
        }

        fun setTitleFrom(text: String): Builder {
            this@DialogDateRange.titleFrom = text
            return this
        }

        fun setTitleTo(@StringRes resId: Int): Builder {
            this@DialogDateRange.titleToRes = resId
            return this
        }

        fun setTitleTo(text: String): Builder {
            this@DialogDateRange.titleTo = text
            return this
        }

        fun setMaxDate(date: Date?): Builder {
            this@DialogDateRange.maxDate = date
            return this
        }

        fun setMinDate(date: Date?): Builder {
            this@DialogDateRange.minDate = date
            return this
        }

        fun setFromDate(date: Date?): Builder {
            this@DialogDateRange.startDate = date
            return this
        }

        fun setToDate(date: Date?): Builder {
            this@DialogDateRange.endDate = date
            return this
        }

        fun setPositiveClickListener(listener: ((startDate: Date, endDate: Date) -> Unit)): Builder {
            this@DialogDateRange.onPositiveClickListener = listener
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