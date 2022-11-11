package com.happyhappyyay.badnutrition.charts

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.Purple200Alpha50

@Composable
fun ChartBase(data: Array<Float>, nutrients: Array<String>?) {
    val axesColor = MaterialTheme.colors.onBackground
    val averageColor = MaterialTheme.colors.secondaryVariant
    val lineColor = MaterialTheme.colors.secondaryVariant
    val pointColor = MaterialTheme.colors.secondary
    val lineColors = arrayOf(axesColor,lineColor,pointColor,averageColor)
    val lineGuideColor1 = MaterialTheme.colors.background
    val lineGuideColor2 = Purple200Alpha50
    val paint = Paint()
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = if(data.size < 32 && nutrients == null) 22F else 28F
        paint.color = axesColor.toArgb()
        Canvas(modifier = Modifier
            .fillMaxSize()) {
            val chartMargin = if(size.height < size.width) size.height/35 else size.width/35
            val yAxisLinePos = size.height - 3.5F * chartMargin
            val topValueOfChart = size.height / 5.25F
            val bottomValueOfChart = yAxisLinePos - topValueOfChart
            val yAxisLines = arrayOf(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
            yAxisLines.forEachIndexed { ind, y ->
                val yCord =
                    bottomValueOfChart - (bottomValueOfChart * ((y * 1F) / 100)) + topValueOfChart
                val guideLineEnd =
                    bottomValueOfChart - (bottomValueOfChart * (((y+10) * 1F) / 100)) + topValueOfChart
                if(ind > 0) {
//                    PUT ALL THIS ONTO THE ACTUAL CHARTS
                    drawRect(
                        if (ind % 2 == 1) lineGuideColor1 else lineGuideColor2,
                        Offset(x = 4.4F * chartMargin, y = yCord),
                        Size(size.width - 4.15F * chartMargin, (yCord - guideLineEnd))
                    )
                }
                drawLine(
                    start = Offset(x = 3.4F * chartMargin, y = yCord),
                    end = Offset(x = 4.4F * chartMargin, y = yCord),
                    color = axesColor,
                    strokeWidth = Stroke.DefaultMiter
                )
                drawIntoCanvas {
                    it.nativeCanvas.drawText("${y}%", 1.75F * chartMargin, yCord + 5, paint)
                }
            }
            drawLine(
                start = Offset(x = 3.4F * chartMargin, y = yAxisLinePos),
                end = Offset(x = size.width - 0.15F * chartMargin, y = yAxisLinePos),
                color = axesColor,
                strokeWidth = Stroke.DefaultMiter
            )
        }
        Row {
            Spacer(modifier = Modifier.width(28.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .width(800.dp)
                ) {
                    if (nutrients != null) {
                        barChart(
                            points = data,
                            nutrients = nutrients,
                            lineColors = lineColors,
                            paint = paint
                        )
                    } else {
                        lineChart(
                            points = data,
                            lineColors = lineColors,
                            paint = paint
                        )
                    }
                }
            }
        }
}

@Preview
@Composable
fun PreviewLineChartInFrame(){
    BadNutritionTheme {
        Chart(type = ChartType.Line, data = MockData().nutrientChartPoints)
    }
}

@Preview
@Composable
fun PreviewLineChartInFrameY(){
    BadNutritionTheme {
        Chart(type = ChartType.Line, data = MockData().nutrientChartPointsY)
    }
}

@Preview
@Composable
fun PreviewBarChartInFrame(){
    BadNutritionTheme {
        Chart(type = ChartType.Bar, data = MockData().nutrientItems)
    }
}
