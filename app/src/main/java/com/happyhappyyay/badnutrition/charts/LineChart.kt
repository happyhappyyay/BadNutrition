package com.happyhappyyay.badnutrition.charts

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August",
"September", "October", "November", "December")

fun DrawScope.lineChart(points: Array<Float>, lineColors:Array<Color>,
                        chartBounds: Array<Float>, paint: Paint
) {
    val axesColor = lineColors[0]
    val lineColor = lineColors[1]
    val pointColor = lineColors[2]
    val averageColor = lineColors[3]
    val topValueOfChart = chartBounds[0]
    val bottomValueOfChart = chartBounds[1]
    val yAxisLinePos = chartBounds[2]
    var date = 1
    var average = 0F
    val offsetPoints = mutableListOf<Offset>()
    var currentXOffset = 3.5F * chartMargin
    val month = points.size/12 + 1
    val xOffset =
        if (points.isNotEmpty()) ((size.width - 4.3F * chartMargin) / points.size) else size.width
    points.forEachIndexed { ind, y ->
        if(points.size < 32) {
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    "${date++}",
                    currentXOffset + 9,
                    yAxisLinePos + 1.65F * chartMargin,
                    paint
                )
            }
            drawLine(
                start = Offset(x = currentXOffset + 9, y = yAxisLinePos),
                end = Offset(x = currentXOffset + 9, y = yAxisLinePos + chartMargin/1.5F),
                color = axesColor,
                strokeWidth = Stroke.DefaultMiter
            )
            drawCircle(
                color = pointColor,
                radius = 6F,
                Offset(
                    x = currentXOffset + 9,
                    y = bottomValueOfChart - (bottomValueOfChart * (y / 100)) + topValueOfChart
                )
            )
        }
        else{
            if(ind != -1 && ind % month == 0) {
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        months[ind/month],
                        currentXOffset,
                        yAxisLinePos + 1.65F * chartMargin,
                        paint
                    )
                }
                drawLine(
                    start = Offset(x = currentXOffset, y = yAxisLinePos),
                    end = Offset(x = currentXOffset, y = yAxisLinePos + chartMargin/1.5F),
                    color = axesColor,
                    strokeWidth = Stroke.DefaultMiter
                )
            }
        }
        offsetPoints.add(
            Offset(
                if(points.size < 32) currentXOffset + 9 else currentXOffset,
                bottomValueOfChart - (bottomValueOfChart * (y / 100)) + topValueOfChart
            )
        )
        currentXOffset += xOffset
        average += y
    }

    average /= points.size
    drawIntoCanvas {
        it.nativeCanvas.drawText("${average}%", 10 * chartMargin, 300F + 5, paint)
    }
    average = bottomValueOfChart - (bottomValueOfChart * (average / 100)) + topValueOfChart
    drawLine(
        start = Offset(x = 2.5F * chartMargin, y = average),
        end = Offset(x = size.width - 0.5F * chartMargin, y = average),
        color = averageColor,
        strokeWidth = Stroke.DefaultMiter,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10F, 10F), 0F)
    )

    drawPoints(
        points = offsetPoints,
        strokeWidth = 2F,
        pointMode = PointMode.Polygon,
        color = lineColor
    )
}