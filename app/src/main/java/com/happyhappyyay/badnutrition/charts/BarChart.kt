package com.happyhappyyay.badnutrition.charts

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import com.happyhappyyay.badnutrition.ui.theme.GraphBarColors
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

fun DrawScope.barChart(
    points: Array<Float>,
    nutrients: Array<String>,
    lineColors:Array<Color>,
    paint: Paint
) {
    val axesColor = lineColors[0]
    val averageColor = lineColors[3]
    val chartMargin = if(size.height < size.width) size.height/35 else size.width/35
    val yAxisLinePos = size.height - 2.25F * chartMargin
    val topValueOfChart = size.height / 5.25F
    val bottomValueOfChart = yAxisLinePos - topValueOfChart
    var average = 0F
    val offsetPoints = mutableListOf<Offset>()
    var currentXOffset = 4.5F * chartMargin
    val month = points.size/12 + 1
    val xOffset =
        if (points.isNotEmpty()) ((size.width - 4.3F * chartMargin) / points.size) else size.width
    val barPaint = Paint(paint)
    barPaint.textAlign = Paint.Align.LEFT
    barPaint.textSize = 32F
    barPaint.typeface = Typeface.DEFAULT_BOLD
    points.forEachIndexed { ind, y ->
        if(points.size < 32) {
            val cordY = bottomValueOfChart - (bottomValueOfChart * (y / 100)) + topValueOfChart
            drawRect(
                color = Color(0, 0, 0, 26),
                Offset(
                    x = currentXOffset,
                    y = cordY - 8
                ),
                size = Size(width = 80F, height =  yAxisLinePos - cordY + 8),
            )
            drawRect(
                color = GraphBarColors[ind % GraphBarColors.size],
                Offset(
                    x = currentXOffset - 5,
                    y = cordY
                ),
                size = Size(width = 70F, height =  yAxisLinePos - cordY - 2 ),
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
                            barPaint
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
                        barPaint
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
    val avgPaint = Paint(paint)
    avgPaint.color = averageColor.toArgb()
    avgPaint.typeface = Typeface.DEFAULT_BOLD
    drawIntoCanvas {
        it.nativeCanvas.drawText("${size.height} h x ${size.width} w", 1.5F * chartMargin, average + chartMargin, avgPaint)
    }
    drawLine(
        start = Offset(x = 0.5F * chartMargin, y = average),
        end = Offset(x = size.width - 0.5F * chartMargin, y = average),
        color = averageColor,
        strokeWidth = Stroke.DefaultMiter,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10F, 10F), 0F)
    )
}