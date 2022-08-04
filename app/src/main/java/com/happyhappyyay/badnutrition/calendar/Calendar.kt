package com.happyhappyyay.badnutrition.calendar

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun Calendar(date: String, setDate: (String) -> Unit) {
    var currentMonthYear by remember { mutableStateOf(date)}
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colors.primaryVariant,
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CalendarHeader(currentMonthYear) { currentMonthYear = it }
            CalendarDays(recordedDays = DAYS, endOfMonth(currentMonthYear), firstDayOfMonth(currentMonthYear)) {
                val newDate = "${currentMonthYear.slice(0..1)}/$it/${currentMonthYear.slice(6..9)}"
                setDate(
                    newDate
                )
            }
        }
    }
}

@Composable
fun CalendarDays(recordedDays: Array<Int>, totalDays: Int, firstDay: Int, setDate: (String) -> Unit) {
    var dataPointer = 0
    var dateOfMonth = 0 - firstDay;
    Column (Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp)){
        for(i in 0..5) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (j in SUN_DAYS.indices) {
                    val calendarDay = if(dateOfMonth in 1..totalDays) dateOfMonth else -1
                    dateOfMonth++;
                    CalendarDay(
                        modifier = Modifier.weight(1f),
                        number = calendarDay,
                        hasData =
                            if(dataPointer < recordedDays.size &&
                                dateOfMonth == recordedDays[dataPointer]){
                                dataPointer++
                                true
                            }
                            else{
                                false
                            },
                        moveTo = {
                            if(calendarDay > 0){
                                val day = if(calendarDay > 9){
                                    "$calendarDay"
                                }
                                else{
                                    "0$calendarDay"
                                }
                                setDate(day)
                            }
                        }
                        )
                }
            }
        }
    }

}

@Composable
fun CalendarDay(modifier: Modifier, number: Int, hasData: Boolean, moveTo: () -> Unit) {
    OutlinedButton(
        onClick = moveTo,
        modifier = modifier.height(40.dp),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        contentPadding = PaddingValues(start = 0.dp, end = 8.dp, top = 0.dp, bottom = 0.dp)
    ){
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            text = if(number != -1) "$number" else "",
            textAlign = TextAlign.End,
            fontWeight = if(hasData)FontWeight.Bold else FontWeight.Normal,
            color = if(hasData)MaterialTheme.colors.secondary else MaterialTheme.colors.onBackground
        )
    }
}

@Composable
fun CalendarHeader(date: String, setCurrentMonth:(String) -> Unit) {
    val monthString = date.slice(0.. 1)
    var monthVal = monthString.toInt()
    val month = when(monthString){
        "01" -> "January"
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
    Column{
        Surface(color = MaterialTheme.colors.primary) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            )
            {
                IconButton(onClick = {
                    var newDate:String
                    if(--monthVal >= 1){
                        newDate = if(monthVal > 9){
                            "$monthVal"
                        } else{
                            "0$monthVal"
                        }
                        newDate += date.slice(2..9)
                    }
                    else{
                        val year = date.slice(6.. 9).toInt().minus(1)
                        newDate = "01/01/${year}"
                    }
                    setCurrentMonth(newDate) }) {
                    Icon(Icons.Rounded.ArrowBack, null)
                }

                CalendarHeaderTitle(month = month)
                IconButton(onClick = {
                    var newDate:String
                    if(++monthVal <= 12){
                        newDate = if(monthVal > 9){
                            "$monthVal"
                        } else{
                            "0$monthVal"
                        }
                        newDate += date.slice(2..9)
                    }
                    else{
                        val year = date.slice(6.. 9).toInt().plus(1)
                        newDate = "01/01/${year}"
                    }
                    setCurrentMonth(newDate) }) {
                    Icon(Icons.Rounded.ArrowForward, null)
                }
            }
            Text(
                modifier = Modifier.padding(end = 4.dp).fillMaxWidth(),
                text = date.slice(6..9),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.h6
            )
        }
        CalendarHeaderDays(SUN_DAYS)
    }
}

@Composable
fun CalendarHeaderTitle(month :String) {
    Text(
        text = month,
        style = MaterialTheme.typography.h4
    )
}

@Composable
fun CalendarHeaderDays(days: Array<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        days.forEach { day ->
                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp).weight(1f),
                    text = day,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                )
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
        CalendarHeader("06/05/2009", setCurrentMonth = { })
    }
}

@Preview
@Composable
fun PreviewCalendarDay(){
    BadNutritionTheme{
        CalendarDay(Modifier,1,true, moveTo = {})
    }
}

@Preview
@Composable
fun PreviewCalendarDays(){
    BadNutritionTheme{
        CalendarDays(recordedDays = DAYS, endOfMonth("07/05/2009"), firstDayOfMonth("07/05/2009"), setDate = {})
    }
}

@SuppressLint("NewApi")
@Preview
@Composable
fun PreviewCalendar(){
    BadNutritionTheme {
        Calendar(date = "06/05/2009", setDate = {})
    }
}

fun endOfMonth(dateString: String): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val format = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val date = LocalDate.parse(dateString, format)
        date.with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth
    } else {
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        cal.time = format.parse(dateString) as Date
        cal.getActualMaximum(Calendar.DATE)
    }
}

fun firstDayOfMonth(dateString: String): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val format = DateTimeFormatter.ofPattern("MM/d/yyyy")
        val date = LocalDate.parse(dateString, format)
        return when(date.with(TemporalAdjusters.firstDayOfMonth()).dayOfWeek.toString()){
            "SUNDAY" -> 0
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
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        cal.time = format.parse(dateString) as Date
        cal.set(Calendar.DAY_OF_MONTH, 1)
        return cal.get(Calendar.DAY_OF_WEEK)
    }
}
