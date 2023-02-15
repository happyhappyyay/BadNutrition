package com.happyhappyyay.badnutrition.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

@Composable
fun Chart(
    modifier: Modifier = Modifier,
    type: GraphType,
    data: Array<Float>,
    heading: String = "Summary",
    onSelected: (BarArea?) -> Unit = {},
    selectedBar: Int = -1
) {
    Card(
        modifier = modifier
            .padding(0.dp)
            .fillMaxWidth(),
        elevation = 0.dp,
        shape = RectangleShape

    ) {
        Surface(
            modifier = Modifier.padding(0.dp),
            shape = RectangleShape

        ) {
            Text(

                text = if (type == GraphType.Line)
                    if (data.size < 32) "Nutrient X through Junio"
                    else "Nutrient X through 2021"
                else heading,
                textAlign = TextAlign.Center
            )
            when (type) {
                GraphType.Bar -> GraphAttempt5(
                    graphOptions = GraphOptions(GraphType.Bar, selectedInd = 2),
                    onSelected = { bar ->
                        onSelected(
                            bar
                        )
                    }) {
                    Text("Calcium")
                }
                else -> {}
//                ChartType.Bar -> ChartBase(data = data, nutrients = MockData().nutritionList)
//                ChartType.Line -> ChartBase(data = data, nutrients = null)
            }

        }
    }
}

@Preview
@Composable
fun PreviewLineChart() {
    BadNutritionTheme {
        Chart(
            type = GraphType.Line,
            data = MockData().nutrientChartPoints,
            selectedBar = -1
        )
    }
}

@Preview
@Composable
fun PreviewBarChart() {
    BadNutritionTheme {
        Chart(
            type = GraphType.Bar,
            data = MockData().nutrientChartPoints,
            selectedBar = -1
        )
    }
}