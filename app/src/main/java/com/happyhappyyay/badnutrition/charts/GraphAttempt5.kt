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
import com.happyhappyyay.badnutrition.ui.theme.*
import com.happyhappyyay.badnutrition.util.*
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

const val MaxZoom = 50F
const val MinZoom = 1F
const val HeadingSize = 50

@Composable
fun GraphAttempt5(
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
    val heightDp = graphOptions.heightDp
    val correctForSpDp = with(LocalDensity.current) { 1.sp.toDp() }
    var enlarged by rememberSaveable { mutableStateOf(false) }
    var rememberZoom by remember { mutableStateOf(1f) }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val height = if (enlarged) screenHeight.dp - HeadingSize.dp else heightDp
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
        val estimatedWidth = with(LocalDensity.current) {
            ((2 * distancePx) * list.size + distancePx).toDp()
        } + (MinToolTipWidth * correctForSpDp / 2)
        val width = if (screenWidth > estimatedWidth) screenWidth else estimatedWidth
        val topPadding = 7.5.dp
        val bottomPadding = 30.dp
        val barsOffset =
            with(LocalDensity.current) { (MinToolTipWidth * correctForSpDp / 2).toPx() }
        var selected by rememberSaveable { mutableStateOf(false) }
        val colorTint by animateColorAsState(
            targetValue = if (selected) MaterialTheme.colors.secondary.copy(alpha = 0.5f) else Color.Transparent
        )
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
            .zoom(
            ) { zoom ->
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
                val pos = (barsOffset + distancePx.times(selectedDataInd)
                    .times(2) - (screenWidthPx / 3)).toInt()
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
                Box {
                    Box(modifier = Modifier.padding(top = 54.dp)) {
                        YAxisGraph(if (enlarged) values else condensedValues)
                    }
                    Column {
                        Spacer(modifier = Modifier.height(topPadding))
                        Row(
                            modifier = Modifier
                                .padding(start = 65.dp)
                                .horizontalScroll(
                                    enabled = !graphOptions.editMode, state = rememberScrollState
                                )
                                .width(width)
                                .weight(1f)
                        ) {
                            Box {
                                XAxisText(
                                    Modifier
                                        .fillMaxHeight()
                                        .padding(start = MinToolTipWidth * correctForSpDp.div(2)),
                                    distanceDp,
                                    list,
                                    graphType != GraphType.Bar
                                )
                                BoxWithConstraints(
                                    modifier = Modifier.fillMaxSize().background(color = Color.Green.copy(alpha = .2F))
                                ) {

                                    val heightDp = this.maxHeight - 56.dp
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
                                    ) { heightDp.toPx() }
                                    val barAreas = list.mapIndexed { index, item ->
                                        BarArea(
                                            index = index,
                                            data = item,
                                            xStart = barsOffset + distancePx.times(index)
                                                .times(2) + distancePx.times(0.5F),
                                            xEnd = barsOffset + distancePx.times(index)
                                                .times(2) + distancePx.times(1.5F),
                                            yStart = (heightPx - (heightPx - (topPadPx + bottomPadPx)) * (item.value / 100F)) - bottomPadPx,
                                            yEnd = 0F
                                        )
                                    }
                                    Box {
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
                                                val startOffset =
                                                    Offset(0F, size.height - bottomPadPx)
                                                val endOffset = Offset(
                                                    size.width, size.height - bottomPadPx
                                                )
//                                              X-Axis Line
                                                drawLine(
                                                    Color.Black, startOffset, endOffset, strokeWidth
                                                )
                                                if (graphType == GraphType.Bar) {
                                                    drawBarGraph4(
                                                        barAreas,
                                                        selectedDataInd,
                                                        selectedColor = color,
                                                        verticalPadding = verticalPadding
                                                    )
                                                } else {
                                                    drawPointGraph4(
                                                        barAreas,
                                                        selectedDataInd,
                                                        selectedColor = color,
                                                        scatterPointSize = if (graphType == GraphType.Scatter) if (enlarged) distancePx.coerceAtLeast(
                                                            8F
                                                        ).coerceAtMost(50F)
                                                        else distancePx.coerceAtLeast(8F)
                                                            .coerceAtMost(35F)
                                                        else -1F,
                                                        verticalPadding = verticalPadding,
                                                        hasAxisTicks = graphOptions.axisTicks
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    if (selectedDataInd != -1) {
//                                        TODO: change to y < 0 && y + height > value + point size?
                                        val bar = barAreas[selectedDataInd]
                                        val xCenter =
                                            if (graphOptions.graphType == GraphType.Bar) (bar.xEnd + bar.xStart) / 2 else bar.xStart
                                        val x =
                                            with(LocalDensity.current) { xCenter.toDp() } - (MinToolTipWidth * correctForSpDp) / 2

//                                        val correctedDistance =
//                                            ((MinToolTipHeight * correctForSpDp.value - MinToolTipHeight) * (1.2F)).coerceAtMost(
//                                                14F
//                                            ).coerceAtLeast(2F).dp

                                        val y = with(LocalDensity.current) {
                                            bar.yStart.toDp().minus(14.dp)
                                        }
                                        Log.d("GraphAttempt6", "$y ${y}")

                                        SimpleToolTip(
                                            offset = DpOffset((-20).dp, y)
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
                                                Text("ba")
                                                Spacer(modifier = Modifier.height(3 * correctForSpDp))

                                            }

                                        }
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
                    visible = selected, enter = slideInHorizontally(), exit = slideOutHorizontally()
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
    Text("time $distancePx")
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
                        .wrapContentHeight(unbounded = true)
                        ,
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
fun GraphHeader(graphType: GraphType, title: @Composable () -> Unit) {
    title()
//    AnimatedVisibility(visible = graphType != GraphType.Bar) {
//        Legend(graphType == GraphType.Line)
//    }
}
fun DrawScope.drawApple(size: Size) {
    val (width, height) = size
    val q = Path().apply {
        reset()
        moveTo(width*.486F,height*.157F)
        quadraticBezierTo(width*.465F,height*.035F,width*.499F,height*.001F)
        lineTo(width*.503F,height*.005F)
        quadraticBezierTo(width*.488F,height*.103F,width*.502F,height*.143F)
        close()
    }
    drawPath(q,Color.Black, style = Stroke(width = 1.dp.toPx(), pathEffect = PathEffect.cornerPathEffect(2.dp.toPx())) )
    val p = Path().apply {
        reset()

        moveTo(width*.493F, height*.867F)
        quadraticBezierTo(width*.296F,height*1.188F,width*.128F,height*.820F)
        quadraticBezierTo(width*-.122F,height*.361F,width*-.081F,height*.163F)
        quadraticBezierTo(width*.283F,height*-.031F,width*.486F,height*.157F)
        quadraticBezierTo(width*.688F,height*-.031F,width*.892F,height*.163F)
        quadraticBezierTo(width*1.093F,height*.358F,width*.858F,height*.820F)
        quadraticBezierTo( width*.686F,height*1.185F,width*.493F,height*.867F)
        close()
    }
    drawPath(p, Color.Black, style = Stroke(width = 1.dp.toPx(), pathEffect = PathEffect.cornerPathEffect(2.dp.toPx())) )

    drawLine(Color.Black, Offset(100F, 100F), Offset(200F,200F))
}

@Preview
@Composable
fun PreviewGraphAttempt5() {
    BadNutritionTheme {
        GraphAttempt5(graphOptions = GraphOptions(GraphType.Bar, ZoomDistanceOption.Day, false, 1))
    }
}

fun DrawScope.drawBarGraph4(
    barAreas: List<BarArea>,
    selectedBarInd: Int,
    selectedColor: Color,
    verticalPadding: Pair<Float, Float>
) {
    val time = measureTimeMillis {
        val (topPadPx, bottomPadPx) = verticalPadding
        val barTextPadding = 8.dp.toPx()
        val barWidth = if (barAreas.isEmpty()) 0F else barAreas[0].xEnd - barAreas[0].xStart
        val xAxisHeight = 2.dp.toPx()
        val barExtSize = barWidth.div(12)
        barAreas.forEach { bar ->
            val distFromGraphBottom = size.height - bar.yStart - bottomPadPx - (xAxisHeight / 2)
            val isSelected = bar.index == selectedBarInd
            if (distFromGraphBottom > 0) {
                drawRoundRect(
                    color = Color.Black.copy(.2f),
                    topLeft = Offset(
                        bar.xEnd - barExtSize, bar.yStart + barExtSize
                    ),
                    size = Size(2 * barExtSize, distFromGraphBottom - xAxisHeight),
                    cornerRadius = CornerRadius(barExtSize / 2, barExtSize / 2)
                )
            }
//            }
            drawRoundRect(
                color = if (isSelected) Purple700 else Purple200,
                topLeft = Offset(bar.xStart, bar.yStart),
                size = Size(barWidth, distFromGraphBottom),
                cornerRadius = CornerRadius(barExtSize / 2, barExtSize / 2)
            )
        }

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

fun DrawScope.drawPointGraph4(
    barAreas: List<BarArea>,
    selectedBarInd: Int,
    selectedColor: Color,
    scatterPointSize: Float = -1F,
    verticalPadding: Pair<Float, Float>,
    hasAxisTicks: Boolean = false
) {
    val graphCreationTime = measureTimeMillis {
        val isScatterPlot = scatterPointSize != -1F
        val xTickMarkSize = 6.dp.toPx()
        val paint = Paint()
        paint.textSize = 12.sp.toPx()
        val (topPadPx, bottomPadPx) = verticalPadding
        val pointSize = if (isScatterPlot) scatterPointSize else 10.dp.toPx()
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
        val bestFitLine = calculateLineOfBestFit(points)
        drawLine(Color.Black, bestFitLine.first, bestFitLine.second)
        val controlPoints1 = mutableListOf<Offset>()
        val controlPoints2 = mutableListOf<Offset>()

        for (i in 1 until points.size) {
            controlPoints1.add(Offset((points[i].x + points[i - 1].x) / 2, points[i - 1].y))
            controlPoints2.add(Offset((points[i].x + points[i - 1].x) / 2, points[i].y))
        }
        if (!isScatterPlot) {
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

        val points1 = barAreas.map { bar ->

            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 20) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Red, center = Offset(bar.xStart, y), radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)

        }
        val bestFitLine1 = calculateLineOfBestFit(points1)
        drawLine(Color.Red, bestFitLine1.first, bestFitLine1.second)
        val points2 = barAreas.map { bar ->

            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 40) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Green, center = Offset(bar.xStart, y), radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val bestFitLine2 = calculateLineOfBestFit(points2)
        drawLine(Color.Green, bestFitLine2.first, bestFitLine2.second)
        val points3 = barAreas.map { bar ->

            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 60) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Blue, center = Offset(bar.xStart, y), radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
        val bestFitLine3 = calculateLineOfBestFit(points3)
        drawLine(Color.Blue, bestFitLine3.first, bestFitLine3.second, 1.dp.toPx())
        val points4 = barAreas.map { bar ->

            val y =
                ((size.height - (size.height - (topPadPx + bottomPadPx)) * ((Math.random() * 80) / 100F)) - bottomPadPx).toFloat()
            if (isScatterPlot) {
                drawCircle(
                    color = Color.Yellow, center = Offset(bar.xStart, y), radius = pointSize - 5
                )
            }
            Offset(bar.xStart, y)
        }
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
        val pointsList = listOf(points, points1, points2, points3, points4)
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

            val pos = pointsList[num][selectedBarInd]
            drawCircle(Purple500.copy(.4f), (pointSize - 5) * 3, pos)
//            drawCircle(Color.Yellow, pointSize, pos)
//            drawCircle(colorsList[num], pointSize, pos, style = Stroke(2.dp.toPx()))
            drawPath(drawApplePath(pos, Size(2*pointSize,2*pointSize), offsetCenter = true), color = Color.Red)
            drawPath(drawApplePath(pos, Size(2*pointSize,2*pointSize), offsetCenter = true),style = Stroke(width = 1.dp.toPx()), color = colorsList[num])
            drawPath(drawAppleStemPath(pos, Size(2*pointSize,2*pointSize), offsetCenter = true),style = Stroke(width = 1.dp.toPx()), color = Color.Black)


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
        it.nativeCanvas.drawText(
            "$graphCreationTime ${barAreas.size}",
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
