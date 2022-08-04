package com.happyhappyyay.badnutrition.day

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.charts.Chart
import com.happyhappyyay.badnutrition.charts.ChartTypes
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

enum class TimeSpanUnit {
    Day, Week, Month, Year
}

@Composable
fun TimeSpan(timeSpan: TimeSpanUnit){
    Column {
        Text(
            text="Summary",
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center
        )
        Chart(ChartTypes.Line, MockData().nutrientChartPoints)
        when (timeSpan) {
            TimeSpanUnit.Day -> Day()
            TimeSpanUnit.Week -> Week()
            TimeSpanUnit.Month -> Month()
            TimeSpanUnit.Year -> Year()
        }
    }
}

@Composable
fun Day() {
//    Foods()
//    Nutrients()
}

@Composable
fun Week() {

}

@Composable
fun Month() {

}

@Composable
fun Year() {

}
@Preview
@Composable
fun PreviewTimeSpan(){
    BadNutritionTheme {
        TimeSpan(TimeSpanUnit.Day)
    }
}