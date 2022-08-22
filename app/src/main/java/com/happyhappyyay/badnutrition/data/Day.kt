package com.happyhappyyay.badnutrition.data

import android.os.Build
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.happyhappyyay.badnutrition.data.food.Portion
import com.happyhappyyay.badnutrition.data.nutrient.NutrientValue
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


@Entity(tableName = "day_table")
data class Day(
    @PrimaryKey
    val dateTime: Long = currentDateInMilliseconds(),
    val nutrients: List<NutrientValue>,
    val foods: List<Portion>
)

fun currentDateInMilliseconds(): Long{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.now().toEpochDay()
    } else {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.time.time
    }
}

fun currentDayString(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.ofEpochMilli(System.currentTimeMillis())
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ISO_DATE)

    } else {
        SimpleDateFormat("yyyyMMMMdd", Locale.US).format(Calendar.getInstance())
    }
}
