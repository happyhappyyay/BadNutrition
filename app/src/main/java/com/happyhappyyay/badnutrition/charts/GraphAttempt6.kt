package com.happyhappyyay.badnutrition.charts

import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.happyhappyyay.badnutrition.ui.theme.*
import com.happyhappyyay.badnutrition.util.*
import com.happyhappyyay.badnutrition.util.ToolTipDirection
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis


const val MinHeightFullValues = 285

@Composable
fun GraphAttempt6(
    modifier: Modifier = Modifier,
    list: List<GraphData> = gList,
    selectedBarInd: Int = -1,
    graphOptions: GraphOptions,
    onSelected: (BarArea?) -> Unit = {},
    onEnlarge: () -> Unit = {},
    onEditable: () -> Unit = {},
    title: @Composable () -> Unit = {}
) {
    Log.d("GRAPH", "recomposed")
    val graphType = graphOptions.graphType
    val selectedDataInd = graphOptions.selectedInd
    val correctForSpDp = with(LocalDensity.current) { 1.sp.toDp() }
    var enlarged by rememberSaveable { mutableStateOf(false) }
    var rememberZoom by remember { mutableStateOf(1f) }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val height = if (enlarged) screenHeight.dp - HeadingSize.dp else graphOptions.heightDp
    val distanceDp by remember(rememberZoom) {
        derivedStateOf {
            (when (graphType) {
                GraphType.Bar -> BAR_DISTANCE
                GraphType.Line -> LINE_DISTANCE
                GraphType.Scatter -> SCATTER_DISTANCE
            } * rememberZoom).dp
        }
    }
    val distancePx = with(LocalDensity.current) { distanceDp.toPx() }

    val rememberCoroutineScope = rememberCoroutineScope()
    val rememberInteractionSource = remember { MutableInteractionSource() }
    val time = measureTimeMillis {
        val portrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
        val rememberScrollState = rememberScrollState()
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp * .9F
        val screenWidthPx = with(LocalDensity.current) { screenWidth.toPx() }
        val graphStartPadDp = 11.dp
        val graphStartPadPx = with(LocalDensity.current) { graphStartPadDp.toPx() }
        val estimatedWidth = with(LocalDensity.current) {
            ((2 * distancePx) * list.size + distancePx + graphStartPadPx).toDp()
        }
        val width = if (screenWidth > estimatedWidth) screenWidth else estimatedWidth
        val topPadding = 7.5.dp
        val bottomPadding = 30.dp
        var selected by rememberSaveable { mutableStateOf(false) }
        val colorTint by animateColorAsState(
            targetValue = if (selected) MaterialTheme.colors.secondary.copy(alpha = 0.5f) else Color.Transparent
        )
        val canvasHeight = height - 56.dp - 16.dp
        val strokeWidth = with(
            LocalDensity.current
        ) { 2.dp.toPx() }
        val verticalPadding = Pair(with(
            LocalDensity.current
        ) { topPadding.toPx() }, with(
            LocalDensity.current
        ) { bottomPadding.toPx() })
        val (topPadPx, bottomPadPx) = verticalPadding
        val heightPx = with(
            LocalDensity.current
        ) { canvasHeight.toPx() }
        val barAreas = list.mapIndexed { index, item ->
            BarArea(
                index = index,
                data = item,
                xStart = distancePx.times(index)
                    .times(2) + distancePx.times(0.5F) + graphStartPadPx,
                xEnd = distancePx.times(index).times(2) + distancePx.times(1.5F) + graphStartPadPx,
                yStart = (heightPx - (heightPx - (topPadPx + bottomPadPx)) * (item.value / 100F)) - bottomPadPx,
                yEnd = 0F
            )
        }
        val graphModifier = modifier
            .height(height)
            .background(color = MaterialTheme.colors.primary.copy(.3f))
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, MaterialTheme.colors.onBackground, Shapes.small)
            .clip(Shapes.small)
            .background(MaterialTheme.colors.background)
        val gestureModifier = if (graphOptions.editMode) graphModifier.clickable {
            selected = !selected
            graphOptions.onEdit(if (selected) 1 else -1)
        }
        else graphModifier
            .indication(rememberInteractionSource, LocalIndication.current)
            .tapGestures(interactionSource = rememberInteractionSource, onLongPress = {
                selected = !selected
                graphOptions.onEdit(1)
            }, onDoubleTap = {
                enlarged = !enlarged
                onEnlarge()
            })
            .animateContentSize()
            .zoom { zoom ->
                graphOptions.onZoom {
                    val pos = rememberScrollState.value
                    val nextZoomVal = zoom * distanceDp.value
                    if (nextZoomVal in MinZoom..MaxZoom) {
                        rememberZoom *= zoom
                        rememberCoroutineScope.launch {
                            rememberScrollState.scrollTo((pos * zoom).toInt())
                        }
                    }
                }
            }
        LaunchedEffect(key1 = selectedDataInd, block = {
            if (selectedDataInd != -1) {
                val pos = (distancePx.times(selectedDataInd).times(2) - (screenWidthPx / 3)).toInt()
                Log.d("graphattempt5", "$pos")
                rememberScrollState.animateScrollTo(pos)
            }
        })
        Box(
            modifier = gestureModifier
        ) {
            GraphHeader(graphType = graphType) {
                Row {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(30.dp),
                    )
                    title()
                    Text("$rememberZoom")
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.padding(top = 54.dp - topPadding + 2.5.dp)) {
                    YAxisGraph(if (screenHeight >= MinHeightFullValues && enlarged) values else condensedValues)
                }
                Row(
                    modifier = Modifier
                        .padding(start = 63.dp)
                        .horizontalScroll(
                            enabled = !graphOptions.editMode, state = rememberScrollState
                        )
                        .width(width)
                ) {
                    Box {
                        XAxisText(
                            Modifier
                                .fillMaxHeight()
                                .padding(start = graphStartPadDp),
                            distanceDp,
                            list,
                            graphType != GraphType.Bar
                        )
                        Box(
                            modifier = Modifier.fillMaxSize()
//                                .background(color = Color.Green.copy(alpha = .2F))
                        ) {
                            val pointSize = if (graphType != GraphType.Bar) {
                                if (graphType == GraphType.Scatter) {
                                    if (enlarged) {
                                        distancePx.coerceAtLeast(8F).coerceAtMost(50F)
                                    } else {
                                        distancePx.coerceAtLeast(8F).coerceAtMost(35F)
                                    }
                                } else {
                                    with(LocalDensity.current) { 10.dp.toPx() }
                                }
                            } else -1F
                            val color = MaterialTheme.colors.secondary
                            val canvasModifier =
                                if (graphOptions.editMode) Modifier
                                    .padding(top = 54.dp)
                                    .fillMaxSize() else Modifier
                                    .padding(top = 54.dp)
                                    .fillMaxSize()
                                    .longPressDrag { offset ->
                                        if (graphType == GraphType.Bar) {
                                            onSelected(barAreas.find { bar -> bar.xStart - distancePx < offset.x && offset.x < bar.xEnd + distancePx })
                                        } else {
                                            onSelected(barAreas.find { bar ->
                                                bar.xStart - distancePx < offset.x && offset.x < bar.xStart + distancePx
                                            })
                                        }
                                    }
                                    .doubleTap {
                                        enlarged = !enlarged
                                        onEnlarge()
                                    }
                            key(rememberZoom) {
                                Canvas(modifier = canvasModifier) {
                                    val startOffset = Offset(
                                        0F, size.height - bottomPadPx - strokeWidth / 2
                                    )
                                    val endOffset = Offset(
                                        size.width, size.height - bottomPadPx - strokeWidth / 2
                                    )

                                    if (graphType == GraphType.Bar) {
                                        drawBarGraph5(
                                            barAreas,
                                            selectedDataInd,
                                            selectedColor = color,
                                            verticalPadding = verticalPadding
                                        )
                                        drawLine(
                                            Color.Black, startOffset, endOffset, strokeWidth
                                        )
                                    } else {
                                        drawLine(
                                            Color.Black, startOffset, endOffset, strokeWidth
                                        )
                                        drawPointGraph5(
                                            barAreas,
                                            selectedDataInd,
                                            selectedColor = color,
                                            isScatterPlot = graphType == GraphType.Scatter,
                                            pointSize = pointSize,
                                            verticalPadding = verticalPadding,
                                            hasAxisTicks = graphOptions.axisTicks,
                                            hasLineOfBestFit = graphOptions.lineOfBestFit
                                        )
                                    }
                                }
                            }
                            if (selectedDataInd != -1) {
//                                TODO: ? maybe use rectangle boundaries to test overlap and adjust
                                val pointSizeAdjustment =
                                    if (graphType != GraphType.Scatter) 1F else with(
                                        LocalDensity.current
                                    ) { SCATTER_DISTANCE.dp.toPx() } / pointSize
                                val toolTipPointSize =
                                    if (graphType == GraphType.Bar) 0.dp else with(LocalDensity.current) { pointSize.toDp() }
                                val minHoverDist = 2F
                                val maxHoverDist = 6F
                                val toolTipHeightSpCorrected = MinToolTipHeight * correctForSpDp
                                val toolTipWidthSpCorrected = MinToolTipWidth * correctForSpDp
                                val bar = barAreas[selectedDataInd]
                                val xCenter =
                                    if (graphOptions.graphType == GraphType.Bar) (bar.xEnd + bar.xStart) / 2 else bar.xStart
                                var x =
                                    with(LocalDensity.current) { xCenter.toDp() } - toolTipWidthSpCorrected / 2
                                val yStart = with(LocalDensity.current) {
                                    bar.yStart.toDp()
                                }
                                var altDir = false
                                var y =
                                    yStart - (maxHoverDist.dp / pointSizeAdjustment) - (toolTipHeightSpCorrected - MinToolTipHeight.dp)
                                Log.d(
                                    "GraphAttempt6",
                                    "$y ${y - toolTipHeightSpCorrected} $toolTipHeightSpCorrected"
                                )
                                if (y - toolTipHeightSpCorrected < -toolTipHeightSpCorrected) {
                                    y =
                                        if (yStart - minHoverDist.dp / pointSizeAdjustment - toolTipHeightSpCorrected < (-43).dp) {
                                            altDir = true
                                            yStart + 54.dp - toolTipHeightSpCorrected / 2
                                        } else {
                                            (yStart.value - toolTipHeightSpCorrected.value).coerceAtMost(
                                                maxHoverDist * pointSizeAdjustment
                                            )
                                                .coerceAtLeast(minHoverDist * pointSizeAdjustment).dp
                                        }
                                }
                                var direction = ToolTipDirection.Down
                                val end = with(LocalDensity.current) { bar.xEnd.toDp() }
                                val start = with(LocalDensity.current) { bar.xStart.toDp() }
                                if (altDir) {
                                    if (end + toolTipWidthSpCorrected + toolTipWidthSpCorrected * .1F > estimatedWidth) {
                                        direction = ToolTipDirection.Left
                                        x = start - (toolTipWidthSpCorrected * 1.1F) - toolTipPointSize
                                    } else {
                                        direction = ToolTipDirection.Right
                                        x =
                                            if (graphType != GraphType.Bar) start + toolTipPointSize else end
                                    }
                                } else {
                                    if (end + toolTipWidthSpCorrected / 2 > estimatedWidth) {
                                        direction = ToolTipDirection.Left
                                        x = start - (toolTipWidthSpCorrected * 1.1F) - toolTipPointSize
                                        y = yStart + 54.dp - toolTipHeightSpCorrected / 2
                                    }
                                    if (start - toolTipWidthSpCorrected / 2 < 0.dp) {
                                        direction = ToolTipDirection.Right
                                        direction = ToolTipDirection.Right
                                        x =
                                            if (graphType != GraphType.Bar) start + toolTipPointSize else end
                                        y = yStart + 54.dp - toolTipHeightSpCorrected / 2
                                    }
                                }
                                SimpleToolTip(
                                    offset = DpOffset(x, y), direction = direction
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text("${bar.data.value.toInt()}00mcg", maxLines = 1)
                                        val correctedInd = selectedDataInd + 24
                                        var month = 0
                                        var day = 1
                                        for (i in DaysPerMonthCum.indices) {
                                            if (DaysPerMonthCum[i] > correctedInd) {
                                                month = i
                                                day =
                                                    DaysPerMonth[month] - (DaysPerMonthCum[month] - correctedInd) + 1
                                                break
                                            }
                                        }
//                                                Text(
//                                                    "${MonthNames[month]} $day",
//                                                    style = MaterialTheme.typography.caption,
//                                                    maxLines = 1
//                                                )
                                        Text("$y $direction", fontSize = 10.sp)
                                        Spacer(modifier = Modifier.height(3 * correctForSpDp))

                                    }

                                }
                            }
                        }
                    }
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .background(colorTint)
            ) {
                AnimatedVisibility(
                    visible = selected,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = "")
                    }
                }
            }
        }
    }
//    Text("time $distancePx")
}

@Preview
@Composable
fun PreviewGraphAttempt6() {
    BadNutritionTheme {
        GraphAttempt6(
            graphOptions = GraphOptions(
                GraphType.Bar,
                ZoomDistanceOption.Day,
                false,
                1
            )
        )
    }
}

fun DrawScope.drawBarGraph5(
    barAreas: List<BarArea>,
    selectedBarInd: Int,
    selectedColor: Color,
    verticalPadding: Pair<Float, Float>
) {
    val time = measureTimeMillis {
        val (_, bottomPadPx) = verticalPadding
        val barWidth = if (barAreas.isEmpty()) 0F else barAreas[0].xEnd - barAreas[0].xStart
        val barExtSize = barWidth.div(12)
        barAreas.forEach { bar ->
            val distFromGraphBottom = (size.height - bottomPadPx) - bar.yStart
            val isSelected = bar.index == selectedBarInd
            if (bar.data.value > 0) {
                drawRoundRect(
                    color = Color.Black.copy(.2f),
                    topLeft = Offset(
                        bar.xEnd - barExtSize, bar.yStart + barExtSize
                    ),
                    size = Size(2 * barExtSize, distFromGraphBottom - barExtSize),
                    cornerRadius = CornerRadius(barExtSize / 2, barExtSize / 2)
                )
            }
            drawRoundRect(
                color = if (isSelected) Purple700 else Purple200,
                topLeft = Offset(bar.xStart, bar.yStart),
                size = Size(barWidth, distFromGraphBottom),
                cornerRadius = CornerRadius(barExtSize / 2, barExtSize / 2)
            )
        }

//        Todo: Remove time paint
        val paint = Paint()
        paint.textSize = 14.sp.toPx()
        val selectedPaint = Paint()
        selectedPaint.typeface = Typeface.DEFAULT_BOLD
        selectedPaint.textSize = 16.sp.toPx()
        selectedPaint.color = selectedColor.copy(1f).toArgb()
    }
    drawIntoCanvas {
        it.nativeCanvas.drawText("$time", 0F, 0F, Paint().apply {
            this.textSize = 45F
        })
    }
}

fun DrawScope.drawPointGraph5(
    barAreas: List<BarArea>,
    selectedBarInd: Int,
    selectedColor: Color,
    isScatterPlot: Boolean,
    pointSize: Float = -1F,
    verticalPadding: Pair<Float, Float>,
    hasAxisTicks: Boolean = false,
    hasLineOfBestFit: Boolean = false
) {
    val graphCreationTime = measureTimeMillis {
        val xTickMarkSize = 6.dp.toPx()
        val paint = Paint()
        paint.textSize = 12.sp.toPx()
        val (topPadPx, bottomPadPx) = verticalPadding
        val selectedBarArea = barAreas.find { bar ->
            bar.index == selectedBarInd
        }

//        X-axis tick marks

        if (hasAxisTicks) {
            barAreas.forEach { bar ->
                drawLine(
                    color = Color.Black,
                    strokeWidth = 1.dp.toPx(),
                    start = Offset(bar.xStart, size.height - bottomPadPx),
                    end = Offset(bar.xStart, size.height - bottomPadPx + (xTickMarkSize / 2))
                )
            }
            for (i in 0 until DaysPerMonthCum.size - 1) {
                drawLine(
                    color = Color.Black, strokeWidth = 1.dp.toPx(), start = Offset(
                        barAreas[DaysPerMonthCum[i] - 24].xStart, size.height - bottomPadPx
                    ), end = Offset(
                        barAreas[DaysPerMonthCum[i] - 24].xStart,
                        size.height - bottomPadPx + xTickMarkSize
                    )
                )
            }
        }

        if (selectedBarArea != null) {
            val selectionBarWidth = (selectedBarArea.xEnd - selectedBarArea.xStart) * 2
//            drawRect(
//                color = Purple500.copy(alpha = .50F),
//                topLeft = Offset(selectedBarArea.xStart - selectionBarWidth, topPadPx),
//                size = Size(selectionBarWidth * 2, size.height - bottomPadPx - topPadPx)
//
//            )
            drawLine(
                Purple500,
                Offset(selectedBarArea.xStart, topPadPx),
                Offset(selectedBarArea.xStart, size.height - bottomPadPx),
                1.dp.toPx()
            )
        }
        val points = barAreas.map { bar ->

            if (isScatterPlot) {
                drawCircle(
                    color = Color.Black,
                    center = Offset(bar.xStart, bar.yStart),
                    radius = pointSize - 5
                )
            }
            Offset(bar.xStart, bar.yStart)
        }
        if (!isScatterPlot) {
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

            drawPath(
                stroke, color = Color.Black, style = Stroke(
                    width = 5f, cap = StrokeCap.Round
                )
            )

            val fillPath = android.graphics.Path(stroke.asAndroidPath()).asComposePath().apply {
                lineTo(points.last().x, size.height - bottomPadPx - 1.5.dp.toPx())
                lineTo(points.first().x, size.height - bottomPadPx - 1.5.dp.toPx())
                close()
            }

            drawPath(
                fillPath,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent, Purple200
                    ), startY = size.height / 3
                ),
            )
        }

//        val points1 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 20) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = Color.Red, center = Offset(bar.xStart, y), radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//
//        }
//        val points2 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 40) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = Color.Green, center = Offset(bar.xStart, y), radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//        }
//        val points3 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 60) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = Color.Blue, center = Offset(bar.xStart, y), radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//        }
//        val points4 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 80) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = Color.Yellow, center = Offset(bar.xStart, y), radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//        }
//        if (isScatterPlot && hasLineOfBestFit) {
//            val bestFitLine = calculateLineOfBestFit(points)
//            drawLine(Color.Black, bestFitLine.first, bestFitLine.second)
//            val bestFitLine1 = calculateLineOfBestFit(points1)
//            drawLine(Color.Red, bestFitLine1.first, bestFitLine1.second)
//            val bestFitLine2 = calculateLineOfBestFit(points2)
//            drawLine(Color.Green, bestFitLine2.first, bestFitLine2.second)
//            val bestFitLine3 = calculateLineOfBestFit(points3)
//            drawLine(Color.Blue, bestFitLine3.first, bestFitLine3.second, 1.dp.toPx())
//        }
//        val points5 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 100) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = Color.Magenta,
//                    center = Offset(bar.xStart, y),
//                    radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//        }
//        val points6 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 100) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = Color.Cyan,
//                    center = Offset(bar.xStart, y),
//                    radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//        }
//        val points7 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 55) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = Color.DarkGray,
//                    center = Offset(bar.xStart, y),
//                    radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//        }
//        val points8 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 11) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = Color.LightGray,
//                    center = Offset(bar.xStart, y),
//                    radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//        }
//        val points9 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 88) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = Color.Unspecified,
//                    center = Offset(bar.xStart, y),
//                    radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//        }
//        val points10 = barAreas.map { bar ->
//
//            val y =
//                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 48) / 100F)) - bottomPadPx).toFloat()
//            if (isScatterPlot) {
//                drawCircle(
//                    color = selectedColor,
//                    center = Offset(bar.xStart, y),
//                    radius = pointSize - 5
//                )
//            }
//            Offset(bar.xStart, y)
//        }
//        val pointsList = listOf(points, points1, points2, points3, points4)
        val colorsList = listOf(
            Color.Black,
            Color.Red,
            Color.Green,
            Color.Blue,
            Color.Magenta,
            Color.Yellow,
            Color.Cyan,
            Color.DarkGray,
            Color.LightGray,
            Color.Unspecified,
            selectedColor
        )
        val num = 0
//        if (!isScatterPlot) {
//            pointsList.forEachIndexed { i, list ->
//                if (i == num) drawPoints(list, PointMode.Polygon, colorsList[num], 1.dp.toPx())
//            }
//        }

        if (selectedBarInd != -1) {
            val pos = points[selectedBarInd]
//            val pos = pointsList[num][selectedBarInd]
            drawCircle(Purple500.copy(.4f), (pointSize - 5) * 3, pos)
//            drawCircle(Color.White, pointSize, pos)
//            drawCircle(colorsList[num], pointSize, pos, style = Stroke(2.dp.toPx()))
            drawPath(
                drawApplePath(pos, Size(2 * pointSize, 2 * pointSize), offsetCenter = true),
                color = Color.Red
            )
            drawPath(
                drawApplePath(pos, Size(2 * pointSize, 2 * pointSize), offsetCenter = true),
                style = Stroke(width = 1.dp.toPx()),
                color = colorsList[num]
            )
            drawPath(
                drawAppleStemPath(
                    pos,
                    Size(2 * pointSize, 2 * pointSize),
                    offsetCenter = true
                ), style = Stroke(width = 1.dp.toPx()), color = Color.Black
            )


//            pointsList.forEach { list ->
//                val pos = list[selectedBarInd]
//                drawCircle(Purple500.copy(.4f), (pointSize - 5) * 3, pos)
//            }
//            pointsList.forEachIndexed { i, list ->
//                val pos = list[selectedBarInd]
//                drawCircle(colorsList[i], pointSize - 2, pos)
//                drawCircle(Color.White, pointSize, pos, style = Stroke(2.dp.toPx()))
//            }
        }

        drawIntoCanvas {
            val textHeight = size.height - verticalPadding.second / 3
            val name = MonthNames[1]
            val textWidth = paint.measureText(name)
            val centerAdjustment = textWidth / 2
            val x = barAreas[DaysPerMonthCum[1 - 1] - 24].xStart - centerAdjustment
            val xStart = if (x <= 0F) 0F else x
            it.nativeCanvas.drawText(
                name, xStart, textHeight, paint
            )
            for (i in 2 until MonthNames.size) {
                val name1 = MonthNames[i]
                val textWidth1 = paint.measureText(name1)
                val centerAdjustment1 = textWidth1 / 2
                it.nativeCanvas.drawText(
                    name1,
                    barAreas[DaysPerMonthCum[i - 1] - 24].xStart - centerAdjustment1,
                    textHeight,
                    paint
                )
            }
        }
    }
    drawIntoCanvas {
        it.nativeCanvas.drawText("$graphCreationTime ${barAreas.size}",
            200F,
            200F,
            Paint().apply { textSize = 50F })
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
