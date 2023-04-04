package com.happyhappyyay.badnutrition.ui.charts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

@Composable
fun Chart(
    modifier: Modifier = Modifier,
    type: GraphType,
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
                GraphType.Bar -> GraphAttempt6(
                    graphOptions = GraphOptions(graphType = GraphType.Bar, selectedInd = 2),
                    onSelected = { bar ->
                        onSelected(
                            bar
                        )
                    }) {
                    Text("Nutrients")
                }
                else -> {}
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
            selectedBar = -1
        )
    }
}