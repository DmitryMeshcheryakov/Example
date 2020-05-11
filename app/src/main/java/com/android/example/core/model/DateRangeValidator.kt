package com.android.example.core.model

import android.os.Parcel
import android.os.Parcelable
import com.android.example.core.utils.formatDate
import com.google.android.material.datepicker.CalendarConstraints
import java.util.*

class DateRangeValidator(
    private val minDate: Long?,
    private val maxDate: Long?,
    private val excludedDates: List<Long>? = null,
    private val excludeWeekends: Boolean = false
) : CalendarConstraints.DateValidator {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.createLongArray()?.toList(),
        parcel.readByte() == 1.toByte()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        minDate?.let { dest?.writeLong(it) }
        maxDate?.let { dest?.writeLong(it) }
        excludedDates?.let { dest?.writeLongArray(it.toLongArray()) }
        dest?.writeByte(if (excludeWeekends) 1 else 0)
    }

    override fun describeContents(): Int = 0

    override fun isValid(date: Long): Boolean {
        val validMinMax = !((minDate ?: Long.MIN_VALUE) > date || (maxDate ?: Long.MAX_VALUE) < date)
        val validWeekend = if (excludeWeekends) {
            val day = Calendar.getInstance().apply { timeInMillis = date }.get(Calendar.DAY_OF_WEEK)
            day != Calendar.SUNDAY && day != Calendar.SATURDAY
        } else true

        val validExcludes = if (excludedDates.isNullOrEmpty()) true else {
            val formatted = Date(date).formatDate(FORMAT)
            !excludedDates.map { Date(it).formatDate(FORMAT) }.contains(formatted)
        }

        return validMinMax && validWeekend && validExcludes
    }

    companion object CREATOR : Parcelable.Creator<DateRangeValidator> {
        override fun createFromParcel(parcel: Parcel): DateRangeValidator {
            return DateRangeValidator(parcel)
        }

        override fun newArray(size: Int): Array<DateRangeValidator?> {
            return arrayOfNulls(size)
        }

        private const val FORMAT = "ddMMyyyy"
    }

}
