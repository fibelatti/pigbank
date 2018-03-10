package com.fibelatti.pigbank.common

import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

object DateHelper {
    const val DATE_FORMAT_STRING = "dd/MM/yyyy"
    @JvmField
    val DATE_FORMAT_SIMPLE = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DATE_FORMAT_STRING, Locale.US)
        }
    }
}

fun dateNow(): String = Date().asString()

fun timestamp(): Long = System.currentTimeMillis()

fun stringAsDate(s: String): Date = DateHelper.DATE_FORMAT_SIMPLE.get().parse(s, ParsePosition(0))

fun intPartsAsDate(year: Int, month: Int, day: Int) = Date(GregorianCalendar(year, month, day).timeInMillis)

fun intPartsAsDateString(year: Int, month: Int, day: Int) = intPartsAsDate(year, month, day).asString()

fun Date.asString(format: DateFormat): String = format.format(this)

fun Date.asString(format: String): String = asString(SimpleDateFormat(format, Locale.US))

fun Date.asString(): String = DateHelper.DATE_FORMAT_SIMPLE.get().format(this)

fun Long.asDateString(): String = Date(this).asString()

fun Float.toFormattedString(format: String = "%.2f"): String = String.format(format, this)
