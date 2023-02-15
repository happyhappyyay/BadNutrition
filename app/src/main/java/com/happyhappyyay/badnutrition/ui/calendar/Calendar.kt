package com.happyhappyyay.badnutrition.ui.calendar

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.util.currentDayString
import com.happyhappyyay.badnutrition.util.monthNumToName
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*

val SUN_DAYS = arrayOf("Su", "Mo", "Tu", "We","Th", "Fr", "Sa")
val MON_DAYS = arrayOf("Mon", "Tue", "Wed","Thu", "Fri", "Sat", "Sun")
val DAYS = arrayOf(3,8,14,19,28,31)
val DAYS_OF_WEEK = arrayOf("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY","FRIDAY", "SATURDAY")

@Composable
fun Calendar(
    date: String,
    setDate: (String) -> Unit
) {
    var curDateStr by rememberSaveable { mutableStateOf(date)}
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.primaryVariant,
        elevation = 4.dp,
        border = BorderStroke(1.dp,MaterialTheme.colors.primaryVariant)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CalendarHeader(curDateStr, {curDateStr = if(it) date else currentDayString()}) { curDateStr = it }
            CalendarDays(
                recordedDays = DAYS,
                curDateStr,
                date
            ) {
                val newDate = curDateStr.slice(0..7) + it
                setDate(
                    newDate
                )
            }

        }
    }
}

@Composable
fun CalendarDays(
    recordedDays: Array<Int>,
    curDateStr: String,
    date: String,
    setDate: (String) -> Unit
) {
    val totalDays = endOfMonth(curDateStr)
    val firstDay = firstDayOfMonth(curDateStr)
    val today = currentDayString()
    var dataInd = 0
    var dateOfMonth = 0 - firstDay
    Column (Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp)){
        for(i in 0..5) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in SUN_DAYS.indices) {
                    val calendarDay = if(dateOfMonth in 1..totalDays) dateOfMonth else -1
                    val hasAdZero = if(calendarDay > 0) calendarDay < 10 else false
                    val correctedDay = "${if(hasAdZero)"0" else ""}${calendarDay}"
                    dateOfMonth++
                    CalendarDay(
                        modifier = Modifier.weight(1f),
                        number = calendarDay,
                        isToday =
                        curDateStr.slice(0..6) == today.slice(0..6)
                                && correctedDay == today.slice(8.. 9),
                        isSelected =
                        curDateStr.slice(0..6) == date.slice(0..6)
                                && correctedDay == date.slice(8.. 9),
                        hasData =
                            if(dataInd < recordedDays.size &&
                                dateOfMonth == recordedDays[dataInd]){
                                dataInd++
                                true
                            }
                            else{
                                false
                            },
                        moveTo = {
                            if(calendarDay > 0){
                                setDate(correctedDay)
                            }
                        }
                        )
                }
            }
        }
    }

}

@Composable
fun CalendarDay(
    modifier: Modifier = Modifier,
    number: Int,
    hasData: Boolean,
    isSelected: Boolean = false,
    moveTo: () -> Unit,
    isToday: Boolean
) {
    OutlinedButton(
        onClick = moveTo,
        modifier = modifier.height(40.dp),
        enabled = !isSelected,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.background),
        contentPadding = PaddingValues(end = 8.dp)
    ){
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            text = if(number != -1) "$number" else "",
            textAlign = TextAlign.End,
            fontWeight = if(hasData)FontWeight.Bold else FontWeight.Normal,
            color = if(hasData)MaterialTheme.colors.secondary else MaterialTheme.colors.onBackground,
            textDecoration = if(isToday)TextDecoration.Underline else TextDecoration.None
        )
    }
}

@Composable
fun CalendarHeader(
    date: String,
    setCurDate: (selected: Boolean) -> Unit,
    setCurrentMonth:(String) -> Unit
) {
    val monthStr = date.slice(5.. 6)
    var monthVal = monthStr.toInt()
    val month = monthNumToName(monthStr)
    val yearStr = date.slice(0.. 3)
    Column{
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Surface(
                modifier = Modifier.padding(4.dp,4.dp),
                color = MaterialTheme.colors.primary,
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
            ) {
                IconButton(onClick = {
                    var newDate:String
                    if(--monthVal >= 1){
                        newDate = if(monthVal > 9){
                            "$monthVal"
                        } else{
                            "0$monthVal"
                        }
                        newDate = "${date.slice(0.. 3)}-${newDate}-${date.slice(8..9)}"
                    }
                    else{
                        val year = date.slice(0.. 3).toInt().minus(1)
                        newDate = "${year}-12-01"
                    }
                    setCurrentMonth(newDate) }) {
                    Icon(Icons.Rounded.ArrowBack, null)
                }
            }
            Surface(
                modifier = Modifier
                    .padding(0.dp, 4.dp)
                    .weight(2F),
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.medium,

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { setCurDate(true) }) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.round_today_alt_black_24
                            ),
                            ""
                        )
                    }
                    CalendarHeaderTitle(month = month, year = yearStr)
                    IconButton(onClick = { setCurDate(false) }) {
                        Icon(Icons.Default.Home, "")
                    }
                }
            }
            Surface(
                modifier = Modifier.padding(4.dp,4.dp),
                color = MaterialTheme.colors.primary,
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
            ) {
                IconButton(onClick = {
                    var newDate:String
                    if(++monthVal <= 12){
                        newDate = if(monthVal > 9){
                            "$monthVal"
                        } else{
                            "0$monthVal"
                        }
                        newDate = "${date.slice(0.. 3)}-${newDate}-${date.slice(8..9)}"
                    }
                    else{
                        val year = date.slice(0.. 3).toInt().plus(1)
                        newDate = "${year}-01-01"
                    }
                    setCurrentMonth(newDate)
                },
                ) {
                    Icon(Icons.Rounded.ArrowForward, null)
                }
            }
        }
        CalendarHeaderDays(SUN_DAYS)
    }
}

@Composable
fun CalendarHeaderTitle(month :String, year: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = month,
            style = MaterialTheme.typography.h6
        )
        Text(
            text = year,
        )
    }

}

@Composable
fun CalendarHeaderDays(days: Array<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        days.forEach { day ->
            Surface(
                modifier = Modifier
                    .padding(start = 1.dp, end = 1.dp, top = 4.dp, bottom = 0.dp)
                    .weight(1F),
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .weight(1f),
                    text = day,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCalendarHeaderDays(){
    BadNutritionTheme{
        CalendarHeaderDays(SUN_DAYS)
    }
}

@Preview
@Composable
fun PreviewCalendarHeader(){
    BadNutritionTheme{
        CalendarHeader("2009-03-01", setCurDate = { }, setCurrentMonth = { })
    }
}

@Preview
@Composable
fun PreviewCalendarDay(){
    BadNutritionTheme{
        CalendarDay(
            Modifier, 1, true, moveTo = {}, isToday = true
        )
    }
}

@Preview
@Composable
fun PreviewCalendarDays(){
    BadNutritionTheme{
        CalendarDays(
            recordedDays = DAYS,
            "2009-07-05",
            "2009-07-06",
            setDate = {},
        )
    }
}

@Preview
@Composable
fun PreviewCalendar(){
    BadNutritionTheme {
        Calendar(date = "2009-06-18", setDate = {})
    }
}

fun endOfMonth(dateString: String): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, format)
        date.with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth
    } else {
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        cal.time = format.parse(dateString) as Date
        cal.getActualMaximum(Calendar.DATE)
    }
}

fun firstDayOfMonth(dateString: String): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, format)
        return when(date.with(TemporalAdjusters.firstDayOfMonth()).dayOfWeek.toString()){
            "MONDAY" -> 1
            "TUESDAY" -> 2
            "WEDNESDAY" -> 3
            "THURSDAY" -> 4
            "FRIDAY" -> 5
            "SATURDAY" -> 6
            else -> 0
        }
    }
    else{
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        cal.time = format.parse(dateString) as Date
        cal.set(Calendar.DAY_OF_MONTH, 1)
        return cal.get(Calendar.DAY_OF_WEEK)
    }
}
