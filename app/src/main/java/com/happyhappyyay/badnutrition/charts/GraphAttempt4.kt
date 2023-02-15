package com.happyhappyyay.badnutrition.charts


import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.Purple500
import com.happyhappyyay.badnutrition.ui.theme.Shapes
import com.happyhappyyay.badnutrition.util.DimensionSubcomposeLayout
import com.happyhappyyay.badnutrition.util.SimpleLegend
import com.happyhappyyay.badnutrition.util.SimpleToolTip
import com.happyhappyyay.badnutrition.util.MinToolTipWidth
import kotlin.system.measureTimeMillis

@Composable
fun GraphAttempt4(
    modifier: Modifier = Modifier,
    graphType: GraphType = GraphType.Line,
    list: List<GraphData> = gList,
    constrainYText: Boolean = false,
    selectedBarNum: Int = -1,
    onSelected: (BarArea?) -> Unit = {},
    title: @Composable () -> Unit = {}
) {
    Log.d("GRAPH", "recomposed")
    val distanceDp = when (graphType) {
        GraphType.Bar -> BAR_DISTANCE.dp
        GraphType.Line -> LINE_DISTANCE.dp
        GraphType.Scatter -> SCATTER_DISTANCE.dp
    }
    val distance = with(LocalDensity.current) { distanceDp.toPx() }
    val time = measureTimeMillis {
        val portrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
        val scrollState = rememberScrollState()
        val graphBackgroundColor = MaterialTheme.colors.background
        val graphGuidelineColor = MaterialTheme.colors.primary.copy(alpha = .2F)
        val colorStops = arrayOf(
            0.0f to graphGuidelineColor,
            0.102f to graphGuidelineColor,
            0.101f to graphBackgroundColor,
            0.2f to graphBackgroundColor,
            0.2f to graphGuidelineColor,
            0.3f to graphGuidelineColor,
            0.301f to graphBackgroundColor,
            0.4f to graphBackgroundColor,
            0.4f to graphGuidelineColor,
            0.5f to graphGuidelineColor,
            0.5f to graphBackgroundColor,
            0.6f to graphBackgroundColor,
            0.6f to graphGuidelineColor,
            0.7f to graphGuidelineColor,
            0.7f to graphBackgroundColor,
            0.8f to graphBackgroundColor,
            0.8f to graphGuidelineColor,
            0.9f to graphGuidelineColor,
            0.9f to graphBackgroundColor,
            1f to graphBackgroundColor,
        )
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp * .9F
        val estimatedWidth = with(LocalDensity.current) {
            ((2 * distance) * list.size + distance).toDp()
        }
        val width = if (screenWidth > estimatedWidth) screenWidth else estimatedWidth
        val topPadding = 10.dp
        val bottomPadding = if (graphType == GraphType.Bar) 10.dp else 30.dp

        Column(
            modifier = modifier
                .background(color = graphBackgroundColor),
        ) {
            title()
            Row(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        Spacer(modifier = Modifier.height(topPadding))
                        Row(
                            modifier = Modifier
                                .horizontalScroll(scrollState)
                                .width(width)
                                .weight(1f)
                        ) {

                            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = topPadding, bottom = bottomPadding)
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colorStops = colorStops,
                                            )
                                        )
                                )
                                val heightDp = this.maxHeight
                                val strokeWidth = with(
                                    LocalDensity.current
                                ) { 2.dp.toPx() }
                                val verticalPadding = Pair(with(
                                    LocalDensity.current
                                ) { topPadding.toPx() },
                                    with(
                                        LocalDensity.current
                                    ) { bottomPadding.toPx() }
                                )
                                val (topPadPx, bottomPadPx) = verticalPadding
                                val heightPx = with(
                                    LocalDensity.current
                                ) { heightDp.toPx() }
                                val min = if (constrainYText) 30 else 20
                                val barMinimum =
                                    heightPx - (heightPx - 2 * verticalPadding.first) * (min / 100F) - bottomPadPx
                                val barAreas = list.mapIndexed { index, item ->
                                    BarArea(
                                        index = index,
                                        data = item,
                                        xStart = distance.times(index) + distance.times(
                                            index
                                        ) - distance.div(
                                            2
                                        )
                                                + distance.times(1.5F),
                                        xEnd = distance.times(index) + distance.times(index) + distance.div(
                                            2
                                        )
                                                + distance.times(1.5F),
                                        yStart = (heightPx - (heightPx - (topPadPx + bottomPadPx)) * (item.value / 100F)) - bottomPadPx,
                                        yEnd = 0F
                                    )
                                }
                                val selectedBar =
                                    if (selectedBarNum > -1 && barAreas.isNotEmpty()) barAreas[selectedBarNum] else null
                                Box {
                                    val infiniteTransition = rememberInfiniteTransition()
                                    val graphSelectedBarColor by infiniteTransition.animateColor(
                                        initialValue = MaterialTheme.colors.secondary.copy(.75f),
                                        targetValue = MaterialTheme.colors.secondary.copy(.1F),
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(1000, easing = LinearEasing),
                                            repeatMode = RepeatMode.Reverse
                                        )
                                    )
                                    val color = MaterialTheme.colors.secondary
                                    key(heightPx) {
                                        Canvas(modifier = Modifier
                                            .fillMaxSize()
                                            .tapOrPress(
                                                scrollableState = scrollState,
                                                onSelect = { offset ->
                                                    val (x: Float, y: Float) = offset
                                                    if (graphType == GraphType.Bar) {
                                                        onSelected(
                                                            barAreas.find { bar -> bar.xStart < x && x < bar.xEnd && (bar.yStart <= y || barMinimum <= y) && y < heightPx - bottomPadPx }
                                                        )
                                                    } else {
                                                        onSelected(
                                                            barAreas.find { bar -> bar.xStart - distance < x && x < bar.xStart + distance && bar.yStart - distance <= y && bar.yStart + distance >= y }
                                                        )
                                                    }
                                                }
                                            )
                                        ) {
                                            val startOffset = Offset(0F, size.height - bottomPadPx)
                                            val endOffset =
                                                Offset(size.width, size.height - bottomPadPx)
                                            drawLine(
                                                Color.Black,
                                                startOffset,
                                                endOffset,
                                                strokeWidth
                                            )
                                            if (graphType == GraphType.Bar) {
                                                drawBarGraph3(
                                                    barAreas,
                                                    selectedBarNum,
                                                    selectedColor = color,
                                                    verticalPadding = verticalPadding
                                                )
                                            } else {
                                                drawPointGraph3(
                                                    barAreas,
                                                    selectedBarNum,
                                                    selectedColor = color,
                                                    verticalPadding = verticalPadding,
                                                    isScatterPlot = graphType == GraphType.Scatter
                                                )
                                            }
                                        }
                                    }
                                }
                                if (selectedBar != null) {

                                    DimensionSubcomposeLayout(mainContent = {
                                        SimpleToolTip {
                                            GraphToolTip(selectedBar = selectedBar)
                                        }
                                    }) { size ->
                                        val density = LocalDensity.current
                                        var x =
                                            with(density) { selectedBar.xEnd.toDp() }.plus(
                                                distanceDp.div(10)
                                            )
                                        var y = with(density) {
                                            selectedBar.yStart.plus(10).toDp()
                                        }

                                        val tipHeight = with(density) { size.height.toDp() }
                                        if (x + MinToolTipWidth.dp > width) {
                                            x = width - MinToolTipWidth.dp - 10.dp
                                        }
                                        if (y + tipHeight > heightDp) {
                                            y =
                                                heightDp - tipHeight - 10.dp
                                        }

                                        SimpleToolTip {
                                            GraphToolTip(selectedBar = selectedBar)
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                if (graphType != GraphType.Bar && !portrait) {
                    SimpleLegend(
                        isHorizontal = false,
                        isLineLegend = graphType == GraphType.Line,
                        legendItems = legendaryItems
                    )
                }
            }
            if (graphType != GraphType.Bar && portrait) {
                SimpleLegend(
                    isLineLegend = graphType == GraphType.Line,
                    legendItems = legendaryItems
                )
            }
        }
    }
}

@Composable
fun GraphToolTip(selectedBar: BarArea) {
    Column(
        modifier = Modifier
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp
            ),
    ) {
        Text(
            selectedBar.data.name,
            modifier = Modifier
                .clickable { }
                .padding(bottom = 8.dp),
            color = MaterialTheme.colors.background,
            style = MaterialTheme.typography.caption,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
        Column {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        color = MaterialTheme.colors.background.copy(
                            .5f
                        )
                    )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(2.dp, Color.DarkGray, shape = Shapes.small)
                        .clip(Shapes.small)
                        .padding(8.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("VAL", color = MaterialTheme.colors.onPrimary)
                    Text(
                        "100mcg",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.caption
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(2.dp, Color.DarkGray, shape = Shapes.small)
                        .padding(8.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("REL", color = MaterialTheme.colors.onPrimary)
                    Text(
                        "100%",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(2.dp, Color.DarkGray, shape = Shapes.small)
                        .padding(8.dp)
                        .sizeIn(50.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("MIN", color = MaterialTheme.colors.onPrimary)
                    Text(
                        "0mcg",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.caption
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(2.dp, Color.DarkGray, shape = Shapes.small)
                        .padding(8.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("MAX", color = MaterialTheme.colors.onPrimary)
                    Text(
                        "1,000,000mcg",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun PreviewGraphAttempt4() {
    BadNutritionTheme {
        GraphAttempt4(selectedBarNum = 1, onSelected = {})
    }
}

fun DrawScope.drawBarGraph3(
    barAreas: List<BarArea>,
    selectedBarInd: Int,
    selectedColor: Color,
    verticalPadding: Pair<Float, Float>
) {
    val time = measureTimeMillis {
        val (topPadPx, bottomPadPx) = verticalPadding
        val barWidth = if (barAreas.isEmpty()) 0F else barAreas[0].xEnd - barAreas[0].xStart
        val barExtSize = barWidth.div(12)
        barAreas.forEach { bar ->
            val distFromGraphBottom = size.height - bar.yStart - topPadPx
            if (bar.index == selectedBarInd) {
                if (distFromGraphBottom > 0) {
                    drawRoundRect(
                        color = selectedColor,
                        topLeft = Offset(
                            bar.xStart - barExtSize,
                            bar.yStart - barExtSize
                        ),
                        size = Size(barWidth + (2 * barExtSize), distFromGraphBottom + barExtSize),
                        cornerRadius = CornerRadius(barExtSize / 2, barExtSize / 2)

                    )
                }
            } else {
                if (distFromGraphBottom > 0) {
                    drawRoundRect(
                        color = Color.Black.copy(.2f),
                        topLeft = Offset(
                            bar.xEnd - barExtSize,
                            bar.yStart + barExtSize
                        ),
                        size = Size(2 * barExtSize, distFromGraphBottom - 5.dp.toPx()),
                        cornerRadius = CornerRadius(barExtSize / 2, barExtSize / 2)
                    )
                }
            }
            drawRoundRect(
                color = Purple500,
                topLeft = Offset(bar.xStart, bar.yStart),
                size = Size(barWidth, distFromGraphBottom),
                cornerRadius = CornerRadius(barExtSize / 2, barExtSize / 2)
            )
        }

        val paint = Paint()
        paint.textSize = 14.sp.toPx()
        paint.typeface = Typeface.DEFAULT_BOLD
//        val facePaint = Paint()
//        facePaint.textSize = 14.sp.toPx()
//        facePaint.typeface = Typeface.DEFAULT_BOLD
//        facePaint.color = Color.White.toArgb()
//        val earPaint = Paint().apply { this.color = Purple500.toArgb()
//        this.textSize = 16.sp.toPx()
//        this.typeface = Typeface.DEFAULT_BOLD}
        val selectedPaint = Paint()
        selectedPaint.typeface = Typeface.DEFAULT_BOLD
        selectedPaint.textSize = 16.sp.toPx()
        selectedPaint.color = selectedColor.copy(1f).toArgb()
        drawIntoCanvas {
            barAreas.forEach { bar ->
                val valString = "${bar.data.value.toInt()}%"
                val textWidth = paint.measureText(valString)
                val centerAdjustment = (barWidth - textWidth) / 2
                val x = bar.xStart + centerAdjustment
                it.nativeCanvas.drawText(
                    "${bar.data.value.toInt()}%",
                    x,
                    bar.yStart - (topPadPx / 2),
                    if (selectedBarInd == bar.index)
                        selectedPaint
                    else
                        paint
                )
//                it.nativeCanvas.drawText(
//                    "^          ^",
//                    bar.xStart + 10,
//                    bar.yStart +18,
//                    if (selectedBarInd == bar.index)
//                        selectedPaint
//                    else
//                        earPaint
//                )
//                it.nativeCanvas.drawText(
//                    "0        0",
//                    bar.xStart + 20,
//                    bar.yStart + 3 * (topPadPx / 2),
//                    if (selectedBarInd == bar.index)
//                        selectedPaint
//                    else
//                        facePaint
//                )
//                it.nativeCanvas.drawText(
//                    "(vvvvv)",
//                    bar.xStart + 20,
//                    bar.yStart + 6F * (topPadPx / 2),
//                    if (selectedBarInd == bar.index)
//                        selectedPaint
//                    else
//                        facePaint
//                )
            }
        }
        val maxTextHeight = size.height * .4F
        translate {
            rotate(270F, Offset(0F, 0F)) {
                drawIntoCanvas {
                    barAreas.forEach { bar ->
                        val barName = bar.data.name
                        val textEnd = paint.breakText(barName, true, maxTextHeight, null)
                        var correctedBarName = barName
                        if (barName.length > textEnd) {
                            correctedBarName = barName.substring(0..textEnd) + "..."
                        }
                        it.nativeCanvas.drawText(
                            correctedBarName,
                            -size.height + bottomPadPx + topPadPx,
                            bar.xStart - topPadPx,
                            if (selectedBarInd == bar.index)
                                selectedPaint
                            else
                                paint
                        )
                    }
                }
            }
        }
    }
    drawIntoCanvas {
        it.nativeCanvas.drawText(
            "$time",
            0F, 0F,
            Paint().apply {
                this.textSize = 45F
            }
        )
    }
}

fun DrawScope.drawPointGraph3(
    barAreas: List<BarArea>,
    selectedBarInd: Int,
    selectedColor: Color,
    verticalPadding: Pair<Float, Float>,
    isScatterPlot: Boolean = false
) {
    val textMaxHeight = measureTimeMillis {
        val xTickMarkSize = 10.dp.toPx()
        val paint = Paint()
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textSize = 30F
        val selectedPaint = Paint()
        selectedPaint.typeface = Typeface.DEFAULT_BOLD
        selectedPaint.textSize = 40F
        selectedPaint.color = selectedColor.copy(1f).toArgb()
        val (topPadPx, bottomPadPx) = verticalPadding
        val pointSize = if (isScatterPlot) SCATTER_DISTANCE.dp.toPx() else LINE_DISTANCE.dp.toPx()
        val barAreasSize = barAreas.size
        val points = barAreas.map { bar ->
            Offset(bar.xStart, bar.yStart)
        }
        val points1 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 20) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Purple500,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)

        }
        val points2 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 40) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = selectedColor,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val points3 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 60) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Red,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val points4 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 80) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Magenta,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val points5 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 100) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Black,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val points6 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 100) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Yellow,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val points7 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 55) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Blue,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val points8 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 11) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Green,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val points9 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 88) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.LightGray,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val points10 = barAreas.map { bar ->
            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 48) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.DarkGray,
                    center = Offset(bar.xStart, y),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        if (!isScatterPlot) {
//            drawPoints(points, PointMode.Polygon, selectedColor, 6.dp.toPx())
            drawPoints(points, PointMode.Polygon, Color.Black, 1.dp.toPx())

            drawPoints(points1, PointMode.Polygon, Color.Red, 1.dp.toPx())
            drawPoints(points2, PointMode.Polygon, Color.Green, 1.dp.toPx())
            drawPoints(points3, PointMode.Polygon, Color.Blue, 1.dp.toPx())
            drawPoints(points4, PointMode.Polygon, Color.Yellow, 1.dp.toPx())
            drawPoints(points5, PointMode.Polygon, Color.Magenta, 1.dp.toPx())
            drawPoints(points6, PointMode.Polygon, Color.Cyan, 1.dp.toPx())
            drawPoints(points7, PointMode.Polygon, Color.DarkGray, 1.dp.toPx())
            drawPoints(points8, PointMode.Polygon, Color.LightGray, 1.dp.toPx())
            drawPoints(points9, PointMode.Polygon, Color.Unspecified, 1.dp.toPx())
            drawPoints(points10, PointMode.Polygon, selectedColor, 1.dp.toPx())
        }
//        for (i in 0 until barAreasSize - 1) {
//            val bar = barAreas[i]
//            val nextBar = barAreas[i + 1]
//            val nextOffset =
//                Offset(nextBar.xStart, nextBar.yStart)
//            val chosenBar = selectedBarInd == bar.index
//            val curOffset = Offset(bar.xStart, bar.yStart)
//
//            drawLine(
//                color = if (chosenBar) selectedColor else Color.Black,
//                strokeWidth = 2F,
//                start = Offset(curOffset.x, size.height - bottomPadPx),
//                end = Offset(curOffset.x, size.height - bottomPadPx + xTickMarkSize)
//            )
//
//            if (!isScatterPlot) {
//                drawLine(
//                    Color.Black,
//                    start = curOffset,
//                    end = nextOffset,
//                    strokeWidth = 1.5.dp.toPx()
//                )
//            }
//
//            if (chosenBar) {
//                drawCircle(
//                    color = selectedColor,
//                    center = Offset(bar.xStart, bar.yStart),
//                    radius = pointSize + 5
//                )
//            }
//            if(isScatterPlot) {
//                drawCircle(
//                    color = GraphBarColors[bar.index % GraphBarColors.size],
//                    center = Offset(bar.xStart, bar.yStart),
//                    radius = pointSize - 5
//                )
//            }
//        }
        val bar = barAreas[barAreasSize - 1]
        val chosenBar = selectedBarInd == bar.index
        val curOffset = Offset(bar.xStart, bar.yStart)

        drawLine(
            if (chosenBar) selectedColor else Color.Black,
            strokeWidth = 2F,
            start = Offset(curOffset.x, size.height - bottomPadPx + 5),
            end = Offset(curOffset.x, size.height - bottomPadPx + 30)
        )
//        if (chosenBar) {
//            drawCircle(
//                color = selectedColor,
//                center = Offset(bar.xStart, bar.yStart),
//                radius = pointSize + 5
//            )
//        }
//        drawCircle(
//            color = GraphBarColors[bar.index % GraphBarColors.size],
//            center = Offset(bar.xStart, bar.yStart),
//            radius = pointSize - 5
//        )

        val adjustmentBase = paint.measureText("8") / 2
        drawIntoCanvas {
            for (i in 0 until barAreasSize) {
                val adjustment: Float = when {
                    i < 9 -> -adjustmentBase
                    i < 99 -> 2 * -adjustmentBase
                    i < 999 -> 3 * -adjustmentBase
                    else -> 4 * -adjustmentBase
                }
                val xOffset = barAreas[i].xStart + adjustment

                it.nativeCanvas.drawText(
                    "${i + 1}",
                    xOffset,
                    size.height - xTickMarkSize + 1.dp.toPx(),
                    paint
                )
            }
        }
    }
    drawIntoCanvas {
        it.nativeCanvas.drawText("$textMaxHeight", 200F, 200F, Paint().apply { textSize = 50F })
    }

}
//
//@Preview("Short Version")
//@Composable
//fun PreviewGraphS(){
//    BadNutritionTheme{
//        Card(modifier = Modifier.fillMaxWidth().height(250.dp)) {
//            GraphAttempt2()
//        }
//    }
//}
//
//@Preview("Long Version")
//@Composable
//fun PreviewGraphL(){
//    BadNutritionTheme{
//        Card(modifier = Modifier.fillMaxWidth().height(50000.dp)) {
//            GraphAttempt2(modifier = Modifier.fillMaxSize())
//        }
//    }
//}
