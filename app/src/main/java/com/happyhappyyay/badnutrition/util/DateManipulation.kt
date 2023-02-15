package com.happyhappyyay.badnutrition.util

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

val DaysPerMonth = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
val DaysPerMonthCum = arrayOf(31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365)
val DaysPerMonthCumLeap = arrayOf(31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366)
val MonthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

fun checkIsLeapYear(year: Int) : Boolean {
    return year % 4 == 0 && year % 100 != 0 && year % 400 == 0
}

fun currentDayString(): String {
    return convertMilliToDay(System.currentTimeMillis())
}

fun convertMilliToDay(date: Long): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.ofEpochMilli(date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ISO_DATE)

    } else {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
    }
}

fun monthNumToName(monthString: String): String {
    return when (monthString) {
        "02" -> "February"
        "03" -> "March"
        "04" -> "April"
        "05" -> "May"
        "06" -> "June"
        "07" -> "July"
        "08" -> "August"
        "09" -> "September"
        "10" -> "October"
        "11" -> "November"
        "12" -> "December"
        else -> "January"
    }
}

fun convertDayToMilli(date: String): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    } else {
        val cal = Calendar.getInstance()
        val year = date.slice(0..3).toInt()
        val month = date.slice(5..6).toInt().minus(1)
        val day = date.slice(8..9).toInt()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.time.time
    }
}

fun incrementMilliDay(date: Long, days: Int): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.ofEpochMilli(date)
            .atZone(ZoneId.systemDefault())
            .plusDays(days.toLong())
            .toInstant()
            .toEpochMilli()
    } else {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date
        cal.add(Calendar.DAY_OF_MONTH, days)
        cal.time.time
    }
}

fun adjustDate(date: String, days: Int): String {
    return convertMilliToDay(incrementMilliDay(convertDayToMilli(date), days))
}