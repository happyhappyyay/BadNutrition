package com.happyhappyyay.badnutrition.ui.charts

import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.happyhappyyay.badnutrition.data.condensedValues
import com.happyhappyyay.badnutrition.data.sampleList
import com.happyhappyyay.badnutrition.data.values
import com.happyhappyyay.badnutrition.ui.theme.*
import com.happyhappyyay.badnutrition.ui.util.*
import com.happyhappyyay.badnutrition.util.DaysPerMonth
import com.happyhappyyay.badnutrition.util.DaysPerMonthCum
import com.happyhappyyay.badnutrition.util.MonthNames
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.system.measureTimeMillis

const val BAR_DISTANCE = 40F
const val SCATTER_DISTANCE = 5F
const val LINE_DISTANCE = 25F

const val MaxZoom = 50F
const val MinZoom = 1F
const val HeadingSize = 50

const val MinHeightFullValues = 285

@Composable
fun GraphAttempt6(
    modifier: Modifier = Modifier,
    count: Int = 0,
    list: List<GraphData> = sampleList,
    selectedBarInd: Int = -1,
    graphOptions: GraphOptions,
    onSelected: (BarArea?) -> Unit = {},
    isEditSelected: Boolean = false,
    onEnlarge: () -> Unit = {},
    onEditable: () -> Unit = {},
    title: @Composable () -> Unit = {}
) {
    Log.d("GRAPH", "recomposed $count")
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
        val dataSize = if (graphType == GraphType.Bar) list.size else graphOptions.dataSize
        val legendItems = createLegendItems(list, dataSize)
        val rememberScrollState = rememberScrollState()
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp * .9F
        val screenWidthPx = with(LocalDensity.current) { screenWidth.toPx() }
        val graphStartPadDp = 11.dp
        val graphStartPadPx = with(LocalDensity.current) { graphStartPadDp.toPx() }
        val estimatedWidth = with(LocalDensity.current) {
            ((2 * distancePx) * dataSize + distancePx + graphStartPadPx).toDp()
        }
        val width = if (screenWidth > estimatedWidth) screenWidth else estimatedWidth
        val topPadding = 7.5.dp
        val bottomPadding = 30.dp
        val colorTint by animateColorAsState(
            targetValue = if (isEditSelected) MaterialTheme.colors.secondary.copy(alpha = 0.5f) else Color.Transparent
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
                xStart = distancePx.times(index % dataSize)
                    .times(2) + distancePx.times(0.5F) + graphStartPadPx,
                xEnd = distancePx.times(index).times(2) + distancePx.times(1.5F) + graphStartPadPx,
                yStart = (heightPx - (heightPx - (topPadPx + bottomPadPx)) * (abs(item.value) / 100F)) - bottomPadPx,
                yEnd = 0F
            )
        }
        Log.d("GraphAttempt6 BARS", "${barAreas.size}")
        val graphModifier = modifier
            .height(height)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, MaterialTheme.colors.onBackground, Shapes.small)
            .clip(Shapes.small)
            .background(MaterialTheme.colors.background)
        val gestureModifier = if (graphOptions.isEditMode) graphModifier.clickable {
            graphOptions.onEdit(count, !isEditSelected)
        }
        else graphModifier
            .indication(rememberInteractionSource, LocalIndication.current)
            .tapGestures(interactionSource = rememberInteractionSource, onLongPress = {
                graphOptions.onEdit(count, !isEditSelected)
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
                rememberScrollState.animateScrollTo(pos)
            }
        })
        Box(
            modifier = gestureModifier
        ) {
            Text("$rememberZoom")
            Box(modifier = Modifier.fillMaxSize()) {
                GraphHeader(
                    graphType = graphType,
                    legendItems = legendItems,
                    hasLegend = list.size > dataSize
                ) {
                    title()
                }
                Box(modifier = Modifier.padding(top = 54.dp - topPadding + 2.5.dp)) {
                    YAxisGraph(if (screenHeight >= MinHeightFullValues && enlarged) values else condensedValues)
                }
                Row(
                    modifier = Modifier
                        .padding(start = 63.dp)
                        .horizontalScroll(
                            enabled = !graphOptions.isEditMode, state = rememberScrollState
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
                                if (graphOptions.isEditMode) Modifier
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
                                            dataSize = dataSize,
                                            isScatterPlot = graphType == GraphType.Scatter,
                                            pointSize = pointSize,
                                            verticalPadding = verticalPadding,
                                            hasAxisTicks = graphOptions.hasXAxisTicks,
                                            hasLineOfBestFit = graphOptions.hasLineOfBestFit,
                                            hasFoodShapes = graphOptions.hasFoodShapeSelectors
                                        )
                                    }
                                }
                            }
                            if (selectedDataInd != -1 && selectedDataInd < barAreas.size) {
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
                                            ).coerceAtLeast(minHoverDist * pointSizeAdjustment).dp
                                        }
                                }
                                var direction = ToolTipDirection.Down
                                val end = with(LocalDensity.current) { bar.xEnd.toDp() }
                                val start = with(LocalDensity.current) { bar.xStart.toDp() }
                                if (altDir) {
                                    if (end + toolTipWidthSpCorrected + toolTipWidthSpCorrected * .1F > estimatedWidth) {
                                        direction = ToolTipDirection.Left
                                        x =
                                            start - (toolTipWidthSpCorrected * 1.1F) - toolTipPointSize
                                    } else {
                                        direction = ToolTipDirection.Right
                                        x =
                                            if (graphType != GraphType.Bar) start + toolTipPointSize else end
                                    }
                                } else {
                                    if (end + toolTipWidthSpCorrected / 2 > estimatedWidth) {
                                        direction = ToolTipDirection.Left
                                        x =
                                            start - (toolTipWidthSpCorrected * 1.1F) - toolTipPointSize
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
                                        val dataValue = bar.data.value
                                        val dataValueAdjusted =
                                            if (dataValue < 0) "~${abs(dataValue)}" else "$dataValue"
                                        Text("${dataValueAdjusted}00mcg", maxLines = 1)
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
                    visible = isEditSelected,
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

@Composable
fun GraphHeader(
    modifier: Modifier = Modifier,
    graphType: GraphType,
    legendItems: Array<LegendItem>,
    hasLegend: Boolean = false,
    title: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Row {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(30.dp),
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                title()
                AnimatedVisibility(visible = hasLegend) {
                    SimpleLegend(
                        isLineLegend = graphType == GraphType.Line, legendItems = legendItems
                    )
                }
            }
        }
    }
}

@Composable
fun YAxisGraph(axisValues: Array<String>) {
    val height = 25.dp
    Column(
        modifier = Modifier
//                +2.5 from change in height from 30 to 25
            .padding(top = 0.dp, bottom = 16.5.dp + 2.5.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        axisValues.forEach { value ->
            Box(
                modifier = Modifier
                    .height(height)
                    .padding(start = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = value,
                    modifier = Modifier
                        .widthIn(min = 50.dp)
                        .wrapContentHeight(unbounded = true),
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.caption,

                    )
                Divider(startIndent = 49.dp, modifier = Modifier.fillMaxWidth())
            }

        }
    }
}

@Composable
fun XAxisText(
    modifier: Modifier = Modifier,
    distanceDp: Dp,
    list: List<GraphData>,
    isTimeBased: Boolean = false,

    ) {
    if (!isTimeBased) {
        val textWidth = distanceDp.times(2)
        Box(
            modifier = modifier, contentAlignment = Alignment.BottomStart
        ) {
            Row {
                list.forEach { data ->
                    Text(
                        data.name,
                        modifier = Modifier
                            .width(textWidth)
                            .height(30.dp)
                            .padding(horizontal = 2.dp),
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewGraphAttempt6() {
    BadNutritionTheme {
        GraphAttempt6(
            graphOptions = GraphOptions(
                graphType = GraphType.Bar
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
            val dataValue = abs(bar.data.value)
            val distFromGraphBottom = (size.height - bottomPadPx) - bar.yStart
            val isSelected = bar.index == selectedBarInd
            if (dataValue > 0.9F) {
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
    dataSize: Int,
    isScatterPlot: Boolean,
    pointSize: Float = -1F,
    verticalPadding: Pair<Float, Float>,
    hasAxisTicks: Boolean = false,
    hasLineOfBestFit: Boolean = false,
    hasFoodShapes: Boolean = false
) {
//    TODO: Animate from 0 to 1 multiply by width rightside
    val graphCreationTime = measureTimeMillis {
        val xTickMarkSize = 6.dp.toPx()
        val paint = Paint()
        paint.textSize = 12.sp.toPx()
        val (topPadPx, bottomPadPx) = verticalPadding
        val dataSets = (barAreas.size / dataSize)
        val selectedBarArea = barAreas.find { bar ->
            bar.index == selectedBarInd
        }
//        TODO: Take care of recomposition? suspended?
        if (barAreas.size >= dataSize) {
            Log.d("PointGRAPH", "${barAreas.size} $dataSize")

//        X-axis tick marks

            if (hasAxisTicks) {

                for (i in 0 until dataSize) {
                    val bar = barAreas[i]
                    drawLine(
                        color = Color.Black,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(bar.xStart, size.height - bottomPadPx),
                        end = Offset(bar.xStart, size.height - bottomPadPx + (xTickMarkSize / 2))
                    )
                }
////            TODO: Change X axis labels to correspond to actual
//            for (i in 0 until DaysPerMonthCum.size - 1) {
//                drawLine(
//                    color = Color.Black, strokeWidth = 1.dp.toPx(), start = Offset(
//                        barAreas[DaysPerMonthCum[i]].xStart, size.height - bottomPadPx
//                    ), end = Offset(
//                        barAreas[DaysPerMonthCum[i]].xStart,
//                        size.height - bottomPadPx + xTickMarkSize
//                    )
//                )
//            }
            }

            if (selectedBarArea != null) {
                drawLine(
                    Purple500,
                    Offset(selectedBarArea.xStart, topPadPx),
                    Offset(selectedBarArea.xStart, size.height - bottomPadPx),
                    1.dp.toPx()
                )
            }
            var count = 0
            for (j in 0 until dataSets) {
                val points = mutableListOf<Offset>()
                val color = GraphBarColors[j]
                for (i in 0 until dataSize) {
                    val bar = barAreas[count++]

                    if (isScatterPlot) {
                        drawCircle(
                            color = color,
                            center = Offset(bar.xStart, bar.yStart),
                            radius = pointSize - 5
                        )
                    }
                    points.add(Offset(bar.xStart, bar.yStart))
                }
                if (!isScatterPlot) {
                    val stroke = generateLinePath(points)
                    drawPath(
                        stroke, color = color, style = Stroke(
                            width = 10f, cap = StrokeCap.Round
                        )
                    )
                    if (dataSets == 1) {
                        val fillPath = Path()
                        fillPath.addPath(stroke)
                        fillPath.lineTo(points.last().x, size.height - bottomPadPx - 1.5.dp.toPx())
                        fillPath.lineTo(points.first().x, size.height - bottomPadPx - 1.5.dp.toPx())
                        fillPath.close()
                        drawPath(
                            fillPath,
                            brush = Brush.verticalGradient(
                                listOf(
                                    color.copy(.8F), color.copy(.2f)
                                ), startY = size.height / 3
                            ),
                        )
                    }
                }

                if (isScatterPlot && hasLineOfBestFit) {
                    val bestFitLine = calculateLineOfBestFit(points)
                    drawLine(color, bestFitLine.first, bestFitLine.second)
                }
            }

            if (selectedBarInd != -1 && selectedBarInd < barAreas.size) {
                val pos = Offset(barAreas[selectedBarInd].xStart, barAreas[selectedBarInd].yStart)
                drawCircle(Purple500.copy(.4f), (pointSize - 5) * 3, pos)
                if (hasFoodShapes) {
                    drawPath(
                        drawApplePath(pos, Size(2 * pointSize, 2 * pointSize), offsetCenter = true),
                        color = Color.Red
                    )
                    drawPath(
                        drawApplePath(pos, Size(2 * pointSize, 2 * pointSize), offsetCenter = true),
                        style = Stroke(width = 1.dp.toPx()),
                        color = Color.Black
                    )
                    drawPath(
                        drawAppleStemPath(
                            pos, Size(2 * pointSize, 2 * pointSize), offsetCenter = true
                        ), style = Stroke(width = 1.dp.toPx()), color = Color.Black
                    )
                } else {
                    drawCircle(Color.White, pointSize, pos)
//                TODO: Color to proper line color
                    drawCircle(Color.Black, pointSize, pos, style = Stroke(2.dp.toPx()))
                }
            }

            drawIntoCanvas {
                val textHeight = size.height - verticalPadding.second / 3
                val name = MonthNames[1]
                val textWidth = paint.measureText(name)
                val centerAdjustment = textWidth / 2
//            val x = barAreas[DaysPerMonthCum[1 - 1] - 24].xStart - centerAdjustment
//            val xStart = if (x <= 0F) 0F else x
//            it.nativeCanvas.drawText(
//                name, xStart, textHeight, paint
//            )
//            for (i in 2 until MonthNames.size) {
//                val name1 = MonthNames[i]
//                val textWidth1 = paint.measureText(name1)
//                val centerAdjustment1 = textWidth1 / 2
//                it.nativeCanvas.drawText(
//                    name1,
//                    barAreas[DaysPerMonthCum[i - 1] - 24].xStart - centerAdjustment1,
//                    textHeight,
//                    paint
//                )
//            }
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