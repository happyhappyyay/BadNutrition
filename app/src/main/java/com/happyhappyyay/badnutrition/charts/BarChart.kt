package com.happyhappyyay.badnutrition.charts

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

val colors = arrayOf(Color(255, 117, 109),Color(243,207,198),Color(77, 77, 255),Color(169, 211, 158),Color(200, 158, 211),
Color(253, 253, 150),Color(244, 194, 194),Color(116, 187, 251),
Color(251, 180, 116),Color(133, 222, 119),Color(237, 101, 114),Color(200, 243, 205),Color(170, 233, 229)
    , Color(211, 211, 211),Color(254, 183, 211))

fun DrawScope.barChart(points: Array<Float>, nutrients: Array<String>, lineColors:Array<Color>,
                        chartBounds: Array<Float>, paint: Paint
) {
    val axesColor = lineColors[0]
    val averageColor = lineColors[3]
    val chartMargin = if(size.height < size.width) size.height/35 else size.width/35
    val topValueOfChart = chartBounds[0]
    val bottomValueOfChart = chartBounds[1] - chartBounds[0]
    val yAxisLinePos = chartBounds[1]
    var average = 0F
    val offsetPoints = mutableListOf<Offset>()
    var currentXOffset = 4.5F * chartMargin
    val month = points.size/12 + 1
    val xOffset =
        if (points.isNotEmpty()) ((size.width - 4.3F * chartMargin) / points.size) else size.width
    paint.textAlign = Paint.Align.LEFT
    paint.textSize = 24F
    points.forEachIndexed { ind, y ->
        if(points.size < 32) {
            val cordY = bottomValueOfChart - (bottomValueOfChart * (y / 100)) + topValueOfChart
            drawRect(
                color = Color(0, 0, 0, 26),
                Offset(
                    x = currentXOffset,
                    y = cordY - 4
                ),
                size = Size(width = 29F, height =  yAxisLinePos - cordY + 1),
            )
            drawRect(
                color = colors[ind%colors.size],
                Offset(
                    x = currentXOffset - 5,
                    y = cordY
                ),
                size = Size(width = 30F, height =  yAxisLinePos - cordY - 2 ),
            )
            translate(-10F,size.height-(1.2F * chartMargin) - 12) {
                rotate(270F, pivot = Offset(0F,0F)) {
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            nutrients[ind],
//                            Y-POSITION
                            chartMargin,
//                            X-POSITION
                            currentXOffset,
                            paint
                        )
                    }
                }
            }
        }
        else{
            if(ind % month == 0) {
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        months[ind/month],
                        currentXOffset,
                        yAxisLinePos + 1.65F * chartMargin,
                        paint
                    )
                }
//              Y-AXIS tick marks - beyond month
                drawLine(
                    start = Offset(x = currentXOffset, y = yAxisLinePos),
                    end = Offset(x = currentXOffset, y = yAxisLinePos + chartMargin /1.5F),
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
        it.nativeCanvas.drawText("${size.height} h x ${size.width} w", 2F * chartMargin, average + chartMargin, paint)
    }
    drawLine(
        start = Offset(x = 2.5F * chartMargin, y = average),
        end = Offset(x = size.width - 0.5F * chartMargin, y = average),
        color = averageColor,
        strokeWidth = Stroke.DefaultMiter,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10F, 10F), 0F)
    )
}