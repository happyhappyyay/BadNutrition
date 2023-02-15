package com.happyhappyyay.badnutrition.util

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

data class LegendItem(val label: String, val color: Color)

@Composable
fun SimpleLegend(
    modifier: Modifier = Modifier,
    isHorizontal: Boolean = true,
    isLineLegend: Boolean = true,
    legendItems: Array<LegendItem>
) {
    Box(modifier = modifier) {
        if (isHorizontal) {
            HorizontalLegend(isLineGraph = isLineLegend, legendItems = legendItems)
        } else {
            VerticalLegend(isLineGraph = isLineLegend, legendItems = legendItems)
        }
    }
}

@Composable
private fun HorizontalLegend(isLineGraph: Boolean, legendItems: Array<LegendItem>) {
    LazyRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(legendItems){ legendItem ->
            SimpleLegendItem(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(0.dp,250.dp),
                isLineGraph = isLineGraph,
                label = legendItem.label,
                color = legendItem.color
            )
        }
    }
}

@Composable
private fun VerticalLegend(isLineGraph: Boolean, legendItems: Array<LegendItem>) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        legendItems.forEach { legendItem ->
            SimpleLegendItem(
                modifier = Modifier
                    .padding(8.dp)
                    .width(150.dp),
                isHorizontal = false,
                isLineGraph = isLineGraph,
                label = legendItem.label,
                color = legendItem.color
            )
        }
    }
}

@Composable
private fun SimpleLegendItem(modifier: Modifier = Modifier, isHorizontal: Boolean = true, isLineGraph: Boolean, label: String, color: Color) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(modifier = Modifier
            .padding(horizontal = 4.dp)
            .width(25.dp),
            contentAlignment = Alignment.Center) {
            if(isLineGraph) {
                Divider(color = color, thickness = 2.dp)
            }
            else {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = color)
                        .size(10.dp)
                )
            }
        }
        Text(
            label,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            maxLines = 1,
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview
@Composable
fun PreviewSimpleLegend() {
    BadNutritionTheme {
        SimpleLegend(
            isHorizontal = true,
            legendItems =
            arrayOf(
                LegendItem(
                    "Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza Pizza ",
                    Color.Red
                ),
                LegendItem("Pasta", Color.Green),
                LegendItem(
                    "Chocolate Chocolate Chocolate Chocolate Chocolate Chocolate Chocolate Chocolate Chocolate ",
                    Color.Cyan
                )
            )
        )
    }
}