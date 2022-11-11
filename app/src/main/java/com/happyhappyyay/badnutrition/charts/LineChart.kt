package com.happyhappyyay.badnutrition.charts

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.happyhappyyay.badnutrition.ui.theme.Purple200Alpha50

val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August",
"September", "October", "November", "December")

fun DrawScope.lineChart(
    points: Array<Float>,
    lineColors:Array<Color>,
    paint: Paint
) {
    val lineGuideColor1 = Color.Black
    val lineGuideColor2 = Purple200Alpha50
    val axesColor = lineColors[0]
    val lineColor = lineColors[1]
    val pointColor = lineColors[2]
    val averageColor = lineColors[3]
    val chartMargin = if(size.height < size.width) size.height/35 else size.width/35
    val yAxisLinePos = size.height - 1.7F * chartMargin
    val topValueOfChart = size.height / 5.25F
    val bottomValueOfChart = yAxisLinePos - topValueOfChart
    var date = 1
    var average = 0F
    val offsetPoints = mutableListOf<Offset>()
    var currentXOffset = chartMargin
    val month = points.size/12 + 1
    val xOffset =
        if (points.isNotEmpty()) ((size.width - 4.3F * chartMargin) / points.size) else size.width
    val yAxisLines = arrayOf(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
    yAxisLines.forEachIndexed { ind, y ->
        val yCord =
            bottomValueOfChart - (bottomValueOfChart * ((y * 1F) / 100)) + topValueOfChart
        val guideLineEnd =
            bottomValueOfChart - (bottomValueOfChart * (((y+10) * 1F) / 100)) + topValueOfChart
//        if(ind > 0) {
////                    PUT ALL THIS ONTO THE ACTUAL CHARTS
//            drawRect(
//                if (ind % 2 == 1) lineGuideColor1 else lineGuideColor2,
//                Offset(x = 3.24F * chartMargin, y = yCord),
//                Size(size.width - 4.15F * chartMargin, (yCord - guideLineEnd))
//            )
//        }
//        drawLine(
//            start = Offset(x = 2.25F * chartMargin, y = yCord),
//            end = Offset(x = 3.25F * chartMargin, y = yCord),
//            color = axesColor,
//            strokeWidth = Stroke.DefaultMiter
//        )
//        drawIntoCanvas {
//            it.nativeCanvas.drawText("${y}%", 1.25F * chartMargin, yCord + 5, paint)
//        }
    }
//    drawLine(
//        start = Offset(x = 3F * chartMargin, y = yAxisLinePos),
//        end = Offset(x = size.width - 0.9F * chartMargin, y = 0F),
//        color = axesColor,
//        strokeWidth = Stroke.DefaultMiter
//    )
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
//          Y-AXIS tick marks - month or less
            drawLine(
                start = Offset(x = currentXOffset, y = yAxisLinePos),
                end = Offset(x = currentXOffset, y = size.height),
                color = axesColor,
                strokeWidth = Stroke.DefaultMiter
            )
//          Points
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
                        yAxisLinePos + 1.5F * chartMargin,
                        paint
                    )
                }
//              Y-AXIS tick marks - beyond month
                drawLine(
                    start = Offset(x = currentXOffset, y = yAxisLinePos),
                    end = Offset(x = currentXOffset, y = yAxisLinePos + chartMargin),
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
    average = bottomValueOfChart - (bottomValueOfChart * (average / 100)) + topValueOfChart
    paint.color = averageColor.toArgb()
    paint.typeface = Typeface.DEFAULT_BOLD
    drawIntoCanvas {
        it.nativeCanvas.drawText("Avg.", 2F * chartMargin, average + chartMargin, paint)
    }
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