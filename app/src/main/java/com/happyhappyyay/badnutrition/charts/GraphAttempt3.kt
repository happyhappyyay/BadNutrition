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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.GraphBarColors
import com.happyhappyyay.badnutrition.util.*
import kotlin.system.measureTimeMillis

@Composable
fun GraphAttempt3(
    modifier: Modifier = Modifier,
    graphType: GraphType = GraphType.Bar,
    list: List<GraphData> = gList,
    constrainYText: Boolean = false,
    selectedBarNum: Int = -1,
    onSelected: (BarArea?) -> Unit = {},
    title: @Composable () -> Unit = {}
) {
    Log.d("GRAPH", "recomposed")
    val distanceDp = when (graphType) {
        GraphType.Bar -> BAR_DISTANCE.plus(12).dp
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
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp * .9F
        val estimatedHeight = with(LocalDensity.current) {
            ((2 * distance) * list.size + distance).toDp()
        }
        val height = if (screenHeight > estimatedHeight) screenHeight else estimatedHeight
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
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxWidth()
                                .height(height)
                                .weight(1f)
                        ) {
                            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = topPadding, bottom = bottomPadding)
                                        .fillMaxSize()
                                        .background(
                                            Brush.horizontalGradient(
                                                colorStops = colorStops
                                            )
                                        )
                                )
                                val widthDp = this.maxWidth
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
                                val widthPx = with(
                                    LocalDensity.current
                                ) { widthDp.toPx() }
                                val min = if (constrainYText) 30 else 20
                                val barMinimum =
                                    widthPx - (widthPx - 2 * verticalPadding.first) * (min / 100F) - bottomPadPx
                                val barAreas = list.mapIndexed { index, item ->
                                    BarArea(
                                        index = index,
                                        data = item,
                                        yStart = distance.div(2).times(index) + distance.times(
                                            index
                                        ) - distance.div(
                                            2
                                        )
                                                + distance,
                                        yEnd = distance.div(2).times(index) + distance.times(index) + distance.div(
                                            2
                                        )
                                                + distance,
                                        xEnd = (widthPx - (widthPx - (topPadPx + bottomPadPx)) * (item.value / 100F)) - bottomPadPx,
                                        xStart = 0F
                                    )
                                }
                                val selectedBar =
                                    if (selectedBarNum > -1 && barAreas.isNotEmpty()) barAreas[selectedBarNum] else null
//                            val selectedBar by remember(selectedPosition, barAreas) {
//                                derivedStateOf {
//                                    barAreas.find { it.xStart < selectedPosition.x && selectedPosition.x < it.xEnd && it.yStart <= selectedPosition.y }
//                                }
//                            }
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
                                    key(widthPx) {
                                        Canvas(modifier = Modifier
                                            .fillMaxSize()
                                            .padding(end = 8.dp)
                                            .background(Color.Green.copy(0.2F))
                                            .tapOrPress(
                                                scrollableState = scrollState,
                                                onSelect = { offset ->
                                                    val (x: Float, y: Float) = offset
                                                    if (graphType == GraphType.Bar) {
                                                        onSelected(
                                                            barAreas.find { bar -> bar.xStart < x && x < bar.xEnd && (bar.yStart <= y || barMinimum <= y) && y < widthPx - bottomPadPx }
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
                                                drawBarGraph2(
                                                    barAreas,
                                                    selectedBarNum,
                                                    selectedColor = graphSelectedBarColor,
                                                    verticalPadding = verticalPadding
                                                )
                                            } else {
                                                drawPointGraph2(
                                                    barAreas,
                                                    selectedBarNum,
                                                    selectedColor = graphSelectedBarColor,
                                                    verticalPadding = verticalPadding,
                                                    isScatterPlot = graphType == GraphType.Scatter
                                                )
                                            }
                                        }
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(),
                                ) {
                                    val colMod = if (constrainYText) Modifier.padding(
                                        top = 13.dp,
                                        bottom = 11.dp
                                    ) else Modifier.padding(top = topPadding, bottom = bottomPadding-topPadding)
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight(),
                                    ) {
                                        gList.forEach { value ->
                                            Text(
                                                text = "${value.name}",
                                                modifier = Modifier.padding(top = distanceDp.value.div(2).dp).height(distanceDp).width(200.dp),
                                                color = MaterialTheme.colors.onBackground,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                                if (selectedBar != null) {
                                    var x = with(LocalDensity.current) { selectedBar.xEnd.toDp() }
                                    var y = with(LocalDensity.current) {
                                        selectedBar.yStart.plus(10).toDp()
                                    }
                                    if (x + MinToolTipWidth.dp > height) {
                                        x = height - MinToolTipWidth.dp - 10.dp
                                    }
//                                    if (y + simpleToolTipHeight.dp > widthDp) {
//                                        y =
//                                            widthDp - (simpleToolTipHeight.dp)
//                                    }
                                    SimpleToolTip {
                                        Column(
                                            modifier = Modifier
                                                .padding(
                                                    vertical = 4.dp,
                                                    horizontal = 8.dp
                                                )
                                                .fillMaxSize(),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                selectedBar.data.name,
                                                color = MaterialTheme.colors.background,
                                                style = MaterialTheme.typography.caption,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Divider(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(2.dp)
                                                    .background(
                                                        color = MaterialTheme.colors.background.copy(
                                                            .5f
                                                        )
                                                    )
                                            )
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    modifier = Modifier.weight(1f),
                                                    text = "Y-Val Nutrient:",
                                                    color = MaterialTheme.colors.background,
                                                    style = MaterialTheme.typography.caption,
                                                    maxLines = 1,
                                                    textAlign = TextAlign.Right,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    modifier = Modifier.weight(1f),
                                                    text = "${selectedBar.data.value}%",
                                                    color = MaterialTheme.colors.background,
                                                    style = MaterialTheme.typography.subtitle1,
                                                    maxLines = 1,
                                                    textAlign = TextAlign.Right,
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }
//                        Spacer(modifier = Modifier.height(topPadding))
                    }
                }
//                if (graphType != GraphType.Bar && !portrait) {
//                    SimpleLegend(legendItems = legendaryItems, horizontal = false)
//                }
            }
            if (graphType != GraphType.Bar) {
                SimpleLegend(legendItems = legendaryItems)
            }
        }
    }
}


@Preview
@Composable
fun PreviewGraphAttempt3() {
    BadNutritionTheme {
        GraphAttempt3(selectedBarNum = 3, onSelected = {})
    }
}

fun DrawScope.drawBarGraph2(
    barAreas: List<BarArea>,
    selectedBarInd: Int,
    selectedColor: Color,
    verticalPadding: Pair<Float, Float>
) {
    val (topPadPx, bottomPadPx) = verticalPadding
    val minLabelPos = (size.width - (size.width - (topPadPx + bottomPadPx)) * (45 / 100F)) - bottomPadPx
    val barTextPadding = BAR_DISTANCE.div(5f)
    val barWidth = if (barAreas.isEmpty()) 0F else barAreas[0].yEnd - barAreas[0].yStart
    barAreas.forEach { bar ->
        val distFromGraphBottom = size.width - bar.xEnd - topPadPx
        val isGreaterThanMinSize = bar.data.value > 55
        if (bar.index == selectedBarInd) {
            drawRoundRect(
                color = selectedColor,
                topLeft = Offset(
                    bar.xStart - 10,
                    bar.yStart - 10
                ),
                size = Size(distFromGraphBottom + 13,barWidth + 20),
                cornerRadius = CornerRadius(10F, 10F)

            )
        } else {
            if (distFromGraphBottom > 0) {
                drawRoundRect(
                    color = Color.Black.copy(.2f),
                    topLeft = Offset(
                        bar.xStart + 10,
                        bar.yStart + 10
                    ),
                    Size(distFromGraphBottom - 7,barWidth),
                    cornerRadius = CornerRadius(7F, 7F)
                )
            }
        }
        drawRoundRect(
            color = GraphBarColors[bar.index % GraphBarColors.size],
            topLeft = Offset(bar.xStart, bar.yStart),
            size = Size(distFromGraphBottom - 2,barWidth),
            cornerRadius = CornerRadius(7F, 7F)
        )
    }

    val paint = Paint()
    paint.textSize = 16.sp.toPx()
    paint.typeface = Typeface.DEFAULT_BOLD
    val selectedPaint = Paint()
    selectedPaint.typeface = Typeface.DEFAULT_BOLD
    selectedPaint.textSize = 14.sp.toPx()
    selectedPaint.color = selectedColor.copy(1f).toArgb()
    drawIntoCanvas {
        barAreas.forEach { bar ->
            val amount = bar.data.value
            val x = if(amount < 55) minLabelPos
            else size.width - bar.xEnd
            it.nativeCanvas.drawText(
                "${bar.data.value}%",
                x,
                (bar.yStart + bar.yEnd)/2,
                if (selectedBarInd == bar.index)
                    selectedPaint
                else
                    paint
            )
        }
    }
}

fun DrawScope.drawPointGraph2(
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
        val pointSize = if (isScatterPlot) SCATTER_DISTANCE else LINE_DISTANCE
        val barAreasSize = barAreas.size
        for (i in 0 until barAreasSize - 1) {
            val bar = barAreas[i]
            val nextBar = barAreas[i + 1]
            val nextOffset =
                Offset(nextBar.xStart, nextBar.yStart)
            val chosenBar = selectedBarInd == bar.index
            val curOffset = Offset(bar.xStart, bar.yStart)

            drawLine(
                color = if (chosenBar) selectedColor else Color.Black,
                strokeWidth = 2F,
                start = Offset(curOffset.x, size.height - bottomPadPx),
                end = Offset(curOffset.x, size.height - bottomPadPx + xTickMarkSize)
            )

            if (!isScatterPlot) {
                drawLine(
                    Color.Black,
                    start = curOffset,
                    end = nextOffset,
                    strokeWidth = 1.5.dp.toPx()
                )
            }

            if (chosenBar) {
                drawCircle(
                    color = selectedColor,
                    center = Offset(bar.xStart, bar.yStart),
                    radius = pointSize + 5
                )
            }
            drawCircle(
                color = GraphBarColors[bar.index % GraphBarColors.size],
                center = Offset(bar.xStart, bar.yStart),
                radius = pointSize - 5
            )
        }
        val bar = barAreas[barAreasSize - 1]
        val chosenBar = selectedBarInd == bar.index
        val curOffset = Offset(bar.xStart, bar.yStart)

        drawLine(
            if (chosenBar) selectedColor else Color.Black,
            strokeWidth = 2F,
            start = Offset(curOffset.x, size.height - bottomPadPx + 5),
            end = Offset(curOffset.x, size.height - bottomPadPx + 30)
        )
        if (chosenBar) {
            drawCircle(
                color = selectedColor,
                center = Offset(bar.xStart, bar.yStart),
                radius = pointSize + 5
            )
        }
        drawCircle(
            color = GraphBarColors[bar.index % GraphBarColors.size],
            center = Offset(bar.xStart, bar.yStart),
            radius = pointSize - 5
        )
        if (isScatterPlot) {

            translate {
                rotate(270F, Offset(0F, 0F)) {
                    drawIntoCanvas {
                        barAreas.forEach { bar ->
                            val barName = bar.data.name
                            it.nativeCanvas.drawText(
                                barName,
                                -size.height,
                                (bar.xStart + bar.xEnd) / 2 - 5,
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
