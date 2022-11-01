package com.happyhappyyay.badnutrition.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

@Composable
fun Chart(type: ChartTypes, data: Array<Float>, heading: String = "Summary") {
    Card(
        modifier = Modifier
            .padding(0.dp)
            .height(225.dp)
            .fillMaxWidth(),
        elevation = 0.dp
    ) {
        Surface(
            modifier = Modifier.padding(0.dp),
            shape = MaterialTheme.shapes.medium,
        ){
            Text(
                modifier = Modifier.height(200.dp),
                text = if(type == ChartTypes.Line)
                        if(data.size < 32) "Nutrient X through Junio"
                        else "Nutrient X through 2021"
                    else heading,
                textAlign = TextAlign.Center)
            when(type){
                ChartTypes.Bar -> ChartBase(data = data, nutrients = MockData().nutritionList)
                ChartTypes.Line -> ChartBase(data = data, nutrients = null)
            }

        }
    }
}

@Preview
@Composable
fun PreviewLineChart(){
    BadNutritionTheme {
        Chart(ChartTypes.Line, MockData().nutrientChartPoints)
    }
}

@Preview
@Composable
fun PreviewBarChart(){
    BadNutritionTheme {
        Chart(ChartTypes.Bar, MockData().nutrientChartPoints)
    }
}