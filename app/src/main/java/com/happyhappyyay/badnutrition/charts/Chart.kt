package com.happyhappyyay.badnutrition.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

@Composable
fun Chart(modifier: Modifier = Modifier, type: ChartType, data: Array<Float>, heading: String = "Summary") {
    Card(
        modifier = modifier
            .padding(0.dp)
            .fillMaxWidth(),
        elevation = 0.dp
    ) {
        Surface(
            modifier = Modifier.padding(0.dp),
            shape = MaterialTheme.shapes.medium,
        ){
            Text(

                text = if(type == ChartType.Line)
                        if(data.size < 32) "Nutrient X through Junio"
                        else "Nutrient X through 2021"
                    else heading,
                textAlign = TextAlign.Center)
            when(type){
                ChartType.Bar -> ChartBase(data = data, nutrients = MockData().nutritionList)
                ChartType.Line -> ChartBase(data = data, nutrients = null)
            }

        }
    }
}

@Preview
@Composable
fun PreviewLineChart(){
    BadNutritionTheme {
        Chart(type = ChartType.Line, data = MockData().nutrientChartPoints)
    }
}

@Preview
@Composable
fun PreviewBarChart(){
    BadNutritionTheme {
        Chart(type = ChartType.Bar, data = MockData().nutrientChartPoints)
    }
}