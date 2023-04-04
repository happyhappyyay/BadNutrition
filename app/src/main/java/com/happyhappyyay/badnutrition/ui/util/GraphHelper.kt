package com.happyhappyyay.badnutrition.ui.util

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import com.happyhappyyay.badnutrition.ui.charts.GraphData
import com.happyhappyyay.badnutrition.ui.theme.GraphBarColors

fun generateLinePath(points: MutableList<Offset>): Path {
    val controlPoints1 = mutableListOf<Offset>()
    val controlPoints2 = mutableListOf<Offset>()

    for (i in 1 until points.size) {
        controlPoints1.add(Offset((points[i].x + points[i - 1].x) / 2, points[i - 1].y))
        controlPoints2.add(Offset((points[i].x + points[i - 1].x) / 2, points[i].y))
    }

    val stroke = Path().apply {
        reset()
        moveTo(points.first().x, points.first().y)
        for (i in 0 until points.size - 1) {
            cubicTo(
                controlPoints1[i].x,
                controlPoints1[i].y,
                controlPoints2[i].x,
                controlPoints2[i].y,
                points[i + 1].x,
                points[i + 1].y
            )
        }
    }
    return stroke
}

fun calculateLineOfBestFit(offsets: List<Offset>): Pair<Offset, Offset> {
    var xBar = 0F
    var yBar = 0F
    offsets.forEach { offset ->
        xBar += offset.x
        yBar += offset.y
    }
    xBar /= offsets.size
    yBar /= offsets.size

    var ySlopeNum = 0F
    var ySlopeDen = 0F
    offsets.forEach { offset ->
        val xFromMean = offset.x - xBar
        ySlopeNum += xFromMean * offset.y - yBar
        ySlopeDen += xFromMean * xFromMean
    }

    //add 1 to avoid divide by 0
    val yIntercept = (ySlopeNum + 1) / (ySlopeDen + 1)
    val firstX = offsets.first().x
    val lastX = offsets.last().x

    return Pair(
        Offset(firstX, yIntercept * firstX + yBar), Offset(lastX, yIntercept * lastX + yBar)
    )
}

fun createLegendItems(rawData: List<GraphData>, dataSize: Int): Array<LegendItem> {
    val numOfItems = rawData.size/dataSize
    var firstItemOfList = -dataSize
    val legendItems = Array(numOfItems){
        firstItemOfList += dataSize
        LegendItem(rawData[firstItemOfList].name, GraphBarColors[it])
    }
    Log.d("LEGENDITEMS","${numOfItems}")
    return legendItems
}