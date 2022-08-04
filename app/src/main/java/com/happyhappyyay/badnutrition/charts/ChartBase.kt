package com.happyhappyyay.badnutrition.charts

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.Purple500Alpha50

const val chartMargin = 20F


@Composable
fun LineChart(data: Array<Float>) {
    val axesColor = MaterialTheme.colors.onBackground
    val averageColor = MaterialTheme.colors.secondaryVariant
    val lineColor = MaterialTheme.colors.secondaryVariant
    val pointColor = MaterialTheme.colors.secondary
    val lineColors = arrayOf(axesColor,lineColor,pointColor,averageColor)
    val lineGuideColor1 = MaterialTheme.colors.background
    val lineGuideColor2 = Purple500Alpha50
    Canvas(modifier = Modifier.fillMaxWidth()) {
        val yAxisLinePos = size.height - 2.25F * chartMargin
        val topValueOfChart = size.height / 5.25F
        val bottomValueOfChart = size.height - 2.25F * chartMargin - topValueOfChart
        val chartBounds = arrayOf(topValueOfChart,bottomValueOfChart, yAxisLinePos)
//        Y-AXIS line
//        drawLine(
//            start = Offset(x = 3F * chartMargin, y = chartMargin),
//            end = Offset(x = 3F * chartMargin, y = yAxisLinePos),
//            color = axesColor,
//            strokeWidth = Stroke.DefaultMiter
//        )

        val paint = Paint()
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = if(data.size < 32) 18F else 16F
        paint.color = axesColor.toArgb()
        val yAxisLines = arrayOf(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
        yAxisLines.forEachIndexed { ind, y ->
            val yCord =
                bottomValueOfChart - (bottomValueOfChart * ((y * 1F) / 100)) + topValueOfChart
            val guideLineEnd =
                bottomValueOfChart - (bottomValueOfChart * (((y+10) * 1F) / 100)) + topValueOfChart
            if(ind > 0) {
                drawRect(
                    if (ind % 2 == 1) lineGuideColor1 else lineGuideColor2,
                    Offset(x = 3.24F * chartMargin, y = yCord),
                    Size(size.width - 4.15F * chartMargin, (yCord - guideLineEnd))
                )
            }
            drawLine(
                start = Offset(x = 2.25F * chartMargin, y = yCord),
                end = Offset(x = 3.25F * chartMargin, y = yCord),
                color = axesColor,
                strokeWidth = Stroke.DefaultMiter
            )
            drawIntoCanvas {
                it.nativeCanvas.drawText("${y}%", 1.25F * chartMargin, yCord + 5, paint)
            }
        }
        drawLine(
            start = Offset(x = 3F * chartMargin, y = yAxisLinePos),
            end = Offset(x = size.width - 0.9F * chartMargin, y = yAxisLinePos),
            color = axesColor,
            strokeWidth = Stroke.DefaultMiter
        )
        lineChart(points = data, lineColors = lineColors, chartBounds = chartBounds, paint = paint)
    }
}

@Preview
@Composable
fun PreviewLineChartInFrame(){
    BadNutritionTheme {
        Chart(type = ChartTypes.Line, data = MockData().nutrientChartPoints)
    }
}

@Preview
@Composable
fun PreviewLineChartInFrameY(){
    BadNutritionTheme {
        Chart(type = ChartTypes.Line, data = MockData().nutrientChartPointsY)
    }
}