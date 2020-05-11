package com.android.example.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val PATTERN_FULL_DATE_WITH_TIME_WITH_SEC = "dd.MM.yyyy, HH:mm:ss"
    val PATTERN_FULL_DATE_WITH_TIME = "dd.MM.yyyy, HH:mm"
    val PATTERN_FULL_DATE = "dd.MM.yyyy"
    val PATTERN_TIME = "HH:mm"
    val PATTERN_EXPIRY = "MM/yy"

    const val DATE_DISTANCE = 24 * 60 * 60 * 1000L

    private val defaultPatterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ssZ",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd",
        "MM/dd/yyyy HH:mm:ss",
        "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
        "MM/dd/yyyy'T'HH:mm:ss.SSSZ",
        "MM/dd/yyyy'T'HH:mm:ss.SSS",
        "MM/dd/yyyy'T'HH:mm:ssZ",
        "MM/dd/yyyy'T'HH:mm:ss",
        "yyyy:MM:dd HH:mm:ss",
        "dd.MM.yyyy HH:mm:ss",
        "yyyyMMdd",
        "dd.MM.yyyy",
        PATTERN_FULL_DATE_WITH_TIME_WITH_SEC,
        PATTERN_FULL_DATE_WITH_TIME,
        PATTERN_FULL_DATE,
        PATTERN_TIME,
        PATTERN_EXPIRY
    )

    fun formatDate(date: Date, pattern: String, locale: Locale = Locale.getDefault()): String {
        val dateFormat = SimpleDateFormat(pattern, locale)
        return dateFormat.format(date)
    }

    fun getDate(date: String?, vararg patterns: String?, locale: Locale = Locale.getDefault()): Date? {
        if (patterns.toList().filterNotNull().isEmpty()) {
            return date?.longFromServer?.let { Date(it) }
        } else {
            patterns.toList().filterNotNull().forEach { pattern ->
                val d = runCatching { SimpleDateFormat(pattern, locale).parse(date) }.getOrNull()
                if (d != null) return d
            }
        }
        return null
    }

    fun getDateOrDefault(date: String?, vararg patterns: String?, locale: Locale = Locale.getDefault()): Date? {
        return getDate(date, *patterns, locale = locale) ?: getDate(date, *defaultPatterns.toTypedArray(), locale = locale)
    }

    fun getFullMonth(date: Date, locale: Locale = Locale.getDefault()): String =
        formatDate(date, "LLLL", locale)

    fun getFullDayOfWeek(date: Date, locale: Locale = Locale.getDefault()): String =
        formatDate(date, "EEEE", locale)

    fun getShortMonth(date: Date, locale: Locale = Locale.getDefault()): String =
        formatDate(date, "MMM", locale)

    fun getShortDayOfWeek(date: Date, locale: Locale = Locale.getDefault()): String =
        formatDate(date, "EEE", locale)

    fun getDay(date: Date): Int = Integer.parseInt(formatDate(date, "dd"))

    fun getMonth(date: Date): Int = Integer.parseInt(formatDate(date, "MM"))

    fun getYear(date: Date): Int = Integer.parseInt(formatDate(date, "yyyy"))

    fun getHours(date: Date): Int = Integer.parseInt(formatDate(date, "HH"))

    fun getMinutes(date: Date): Int = Integer.parseInt(formatDate(date, "mm"))

    fun getSeconds(date: Date): Int = Integer.parseInt(formatDate(date, "ss"))

    private fun getDefaultDate(date: String?, locale: Locale = Locale.getDefault()): Date? {
        defaultPatterns.forEach {
            runCatching {
                SimpleDateFormat(it, locale).parse(date)
            }.getOrNull()?.let {
                return it
            }
        }
        return null
    }
}

fun String.getDate(vararg patterns: String?): Date? {
    return DateUtils.getDate(this, *patterns)
}

fun String.getDateOrDefault(vararg patterns: String?): Date? {
    return DateUtils.getDateOrDefault(this, *patterns)
}

fun Date.formatDate(pattern: String): String {
    return DateUtils.formatDate(this, pattern)
}

fun Date.formatDateForServer(pattern: String): String {
    return DateUtils.formatDate(this, pattern, Locale.ENGLISH)
}

val Date.formatExpiry: String
    get() = this.formatDate(DateUtils.PATTERN_EXPIRY)

val Date.formatTime: String
    get() = this.formatDate(DateUtils.PATTERN_TIME)

val Date.formatDate: String
    get() = this.formatDate(DateUtils.PATTERN_FULL_DATE)

val Date.formatDateWithTime: String
    get() {
        val date = this.formatDate(DateUtils.PATTERN_FULL_DATE_WITH_TIME)
        return if (!false) date
        else date.replace("00:00", "")
    }

val Date.formatDateWithTimeWithSeconds: String
    get() {
        val date = this.formatDate(DateUtils.PATTERN_FULL_DATE_WITH_TIME_WITH_SEC)
        return if (!false) date
        else date.replace("00:00:00", "")
    }

fun Date.addDays(days: Int?): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    days?.let { calendar.add(Calendar.DAY_OF_MONTH, it) }
    return calendar.time
}

fun Date.addWeeks(weeks: Int?): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    weeks?.let { calendar.add(Calendar.WEEK_OF_YEAR, it) }
    return calendar.time
}

fun Date.addMonths(months: Int?): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    months?.let { calendar.add(Calendar.MONTH, it) }
    return calendar.time
}

fun Date.addYears(years: Int?): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    years?.let { calendar.add(Calendar.YEAR, it) }
    return calendar.time
}

val Date.toStartOfDay: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

val Date.toEndOfDay: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }

val Date.toStartOfWeek: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getMinimum(Calendar.DAY_OF_WEEK))
        return calendar.time
    }

val Date.toEndOfWeek: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getMaximum(Calendar.DAY_OF_WEEK))
        return calendar.time
    }

val Date.toStartOfMonth: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH))
        return calendar.time
    }

val Date.toEndOfMonth: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH))
        return calendar.time
    }


val Date.toHourAgo: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.HOUR_OF_DAY, -1)
        return calendar.time
    }

val Date.toYesterday: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.time
    }

val Date.toWeekAgo: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        return calendar.time
    }

val Date.toMonthAgo: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.MONTH, -1)
        return calendar.time
    }

val Date.to3MonthAgo: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.MONTH, -3)
        return calendar.time
    }

val Date.to6MonthAgo: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.MONTH, -6)
        return calendar.time
    }

val Date.toYearAgo: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.YEAR, -1)
        return calendar.time
    }

val Date.isToday: Boolean
    get() {
        val calendar1 = Calendar.getInstance()
        calendar1.time = this
        val calendar2 = Calendar.getInstance()
        calendar2.time = Date()


        return calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
    }

val Date.isYesterday: Boolean
    get() {
        val calendar1 = Calendar.getInstance()
        calendar1.time = this
        val calendar2 = Calendar.getInstance()
        calendar2.add(Calendar.DAY_OF_MONTH, -1)

        return calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
    }

val Date.isWeekAgo: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        return LongRange(
            calendar.timeInMillis - DateUtils.DATE_DISTANCE,
            calendar.timeInMillis + DateUtils.DATE_DISTANCE
        ).contains(this.time)
    }

val Date.isMonthAgo: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        return LongRange(
            calendar.timeInMillis - DateUtils.DATE_DISTANCE,
            calendar.timeInMillis + DateUtils.DATE_DISTANCE
        ).contains(this.time)
    }

val Date.is3MonthAgo: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        return LongRange(
            calendar.timeInMillis - DateUtils.DATE_DISTANCE,
            calendar.timeInMillis + DateUtils.DATE_DISTANCE
        ).contains(this.time)
    }

val Date.is6MonthAgo: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -6)
        return LongRange(
            calendar.timeInMillis - DateUtils.DATE_DISTANCE,
            calendar.timeInMillis + DateUtils.DATE_DISTANCE
        ).contains(this.time)
    }

val Date.isYearAgo: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -1)
        return LongRange(
            calendar.timeInMillis - DateUtils.DATE_DISTANCE,
            calendar.timeInMillis + DateUtils.DATE_DISTANCE
        ).contains(this.time)
    }
fun Long?.getDateStr() : String = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", java.util.Date(this ?: Date().time)).toString()
