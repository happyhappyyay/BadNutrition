package com.happyhappyyay.badnutrition.day

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

@Composable
fun Chart() {
    ChartFrame()
}

@Composable
fun ChartFrame() {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(225.dp)
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Surface(
            modifier = Modifier.padding(8.dp),
            elevation = 8.dp,
            color = MaterialTheme.colors.background,
            shape = MaterialTheme.shapes.medium,
        ){
            Text(
                modifier = Modifier.height(200.dp),
                text = "Progress of Deez Nutz", textAlign = TextAlign.Center)
            ChartContent(points = MockData().nutrientChartPoints)
        }
    }
}
@Composable
fun ChartContent(points: Array<Float>) {
    val lineColor = MaterialTheme.colors.secondaryVariant
    val pointColor = MaterialTheme.colors.secondary
    val axesColor = MaterialTheme.colors.onBackground
    val averageColor = MaterialTheme.colors.secondaryVariant
    Canvas(modifier = Modifier.fillMaxWidth()) {
        val chartMargin = 20F
        val yAxisLinePos = size.height - 1.5F * chartMargin
        val xOffset = if(points.isNotEmpty()) ((size.width - 3*chartMargin) / points.size) else size.width
        var currentXOffset = 1.6F*chartMargin
        val axisXOffset = xOffset * 1.85F
        val topValueOfChart = size.height /5.25F
        val bottomValueOfChart = size.height - 2.25F*chartMargin - topValueOfChart

        drawLine(
            start = Offset(x = 2.5F*chartMargin, y = chartMargin),
            end = Offset(x = 2.5F*chartMargin, y = yAxisLinePos),
            color = axesColor,
            strokeWidth = Stroke.DefaultMiter
        )

        drawLine(
            start = Offset(x = 2.5F*chartMargin, y = yAxisLinePos),
            end = Offset(x = size.width - 0.5F * chartMargin, y = yAxisLinePos),
            color = axesColor,
            strokeWidth = Stroke.DefaultMiter
        )
        currentXOffset *= 2
        var average = 0F
        val offsetPoints = mutableListOf<Offset>()
        var date = 1
        val paint = android.graphics.Paint()
        paint.textAlign = android.graphics.Paint.Align.CENTER
        paint.textSize = if(points.size <= 31) 16F else 13F
        val yAxisLines = arrayOf(0,10,20,30,40,50,60,70,80,90,100)
        yAxisLines.forEach { y ->
            val yCord = bottomValueOfChart-(bottomValueOfChart*((y*1F)/100)) + topValueOfChart
            drawLine(
                start = Offset(x = 2 * chartMargin,y = yCord),
                end = Offset(x = 3 * chartMargin, y = yCord) ,
                color = axesColor,
                strokeWidth = Stroke.DefaultMiter
            )
            drawIntoCanvas {
                it.nativeCanvas.drawText("${y}%", chartMargin,yCord+5, paint)
            }
        }
        points.forEach { y ->
            drawIntoCanvas { it.nativeCanvas.drawText("${date++}", currentXOffset, yAxisLinePos + 1.3F * chartMargin,paint) }
            drawCircle(
                color = pointColor,
                radius = 6F,
                Offset(x = currentXOffset, y = bottomValueOfChart - (bottomValueOfChart*(y/100)) + topValueOfChart)
            )
            drawLine(
                start = Offset(x = currentXOffset, y = yAxisLinePos - chartMargin/2),
                end = Offset(x = currentXOffset, y = yAxisLinePos + chartMargin/2),
                color = axesColor,
                strokeWidth = Stroke.DefaultMiter
            )
            offsetPoints.add(Offset(currentXOffset, bottomValueOfChart - (bottomValueOfChart*(y/100)) + topValueOfChart))
            currentXOffset += xOffset
            average += y
        }

        average /= points.size
        drawIntoCanvas {
            it.nativeCanvas.drawText("${average}%", 10*chartMargin,300F+5, paint)
        }
        average = bottomValueOfChart-(bottomValueOfChart*(average/100)) + topValueOfChart
        drawLine(
            start = Offset(x = 2.5F * chartMargin, y = average),
            end = Offset(x = size.width - 0.5F * chartMargin, y = average),
            color = averageColor,
            strokeWidth = Stroke.DefaultMiter,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10F,10F),0F)
        )

        drawPoints(
            points = offsetPoints,
            strokeWidth =  2F,
            pointMode = PointMode.Polygon,
            color = lineColor
        )

//        drawPoints(
//            points = offsetPoints,
//            strokeWidth =  10F,
//            pointMode = PointMode.Points,
//            color = pointColor
//        )

    }
}

@Preview
@Composable
fun PreviewChart(){
    BadNutritionTheme {
        Chart()
    }
}

@Preview
@Composable
fun PreviewChartFrame(){
    BadNutritionTheme {
        ChartFrame()
    }
}