package com.happyhappyyay.badnutrition.ui.charts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.GraphBarColors
import com.happyhappyyay.badnutrition.ui.theme.Purple500
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

const val DonutSize = 250
const val NoSelectionHeading = "Daily Composition"

val data = arrayListOf(
    GraphData("Calcium", 20F),
    GraphData("Magnesium", 10F),
    GraphData("Carbohydrates", 100F),
    GraphData("Phosphate", 2F),
    GraphData("Protein", 10F),
    GraphData("Calories", 100F),
    GraphData("Ash", 2F),
    GraphData("Water", 10F),
    GraphData("Saturated Fat", 10F),
    GraphData("Unsaturated Fat", 20F),
    GraphData("Fiber", 1F),
    GraphData("Sugar", 100F),
    GraphData("Iron", 10F),
)

@Composable
fun DonutChart(
    graphData: List<GraphData> = data
) {
    val values = graphData.map { datum ->
        datum.value
    }

    val names = graphData.map { datum ->
        datum.name
    }

    var selectedInd by remember { mutableStateOf(-1) }
    val isSelected = selectedInd > -1
    val barCompletion = remember {
        Animatable(
            initialValue = 0F
        )
    }
    val portionSizeList = remember { mutableStateListOf<Float>() }
    val animateList = remember { mutableStateListOf<State<Float>>() }

    val height = with(LocalDensity.current) { DonutSize.dp.toPx() }
    val width = with(LocalDensity.current) { DonutSize.dp.toPx() }
    val dpToPxAdj = with(LocalDensity.current) { 1.dp.toPx() }
    val canvasPadding = DonutSize * 0.08

    LaunchedEffect(key1 = null) {
        barCompletion.animateTo(1F, animationSpec = tween(1400))
    }
    val selectedWidth = (DonutSize / 10 * 1.5F) * dpToPxAdj
    val unselectedWidth = (DonutSize / 10) * dpToPxAdj
    values.forEachIndexed { ind, _ ->
        portionSizeList.add(unselectedWidth)
        animateList.add(animateFloatAsState(targetValue = portionSizeList[ind]))
    }
    val sum = values.sum()

    val proportions = values.map { value ->
        value / sum
    }
    val sweepAngles = proportions.map { proportion ->
        360 * proportion
    }

    val cumulativeAngles = mutableListOf<Float>()
    cumulativeAngles.add(sweepAngles.first())
    portionSizeList.add(unselectedWidth)
    for (i in 1 until sweepAngles.size) {
        val sumOfPreviousAngles = sweepAngles[i] + cumulativeAngles[i - 1]
        cumulativeAngles.add(sumOfPreviousAngles)
        portionSizeList.add(unselectedWidth)
    }
    var startAngle = 0F
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Card(backgroundColor = MaterialTheme.colors.primary.copy(.2f), modifier = Modifier
            .padding(8.dp)
            .tapDonut(size = Size(width, height)) { size, coordinates ->
                val clickedDistance = coordinateDistanceFromCenter(size, coordinates)
                val clickedAngle =
                    pointAngleConversion(size.width, size.height, coordinates.x, coordinates.y)
                val radiusStartDistance = (size.width - (size.width * .28)) / 2
                val radiusEndDistance = (size.width - (size.width * .05)) / 2
                for (i in cumulativeAngles.indices) {
                    val angle = cumulativeAngles[i]
                    if (clickedAngle <= angle && clickedDistance >= radiusStartDistance && clickedDistance <= radiusEndDistance) {
                        if (selectedInd != i) {
    //                        keep index check (vs. isSelected) so that most recent value is passed
                            if (selectedInd > -1) {
                                portionSizeList[selectedInd] = unselectedWidth
                            }
                            selectedInd = i
                            portionSizeList[selectedInd] = selectedWidth
                        }
                        break
                    }
                }
            }) {
            Canvas(
                Modifier
                    .size(DonutSize.dp)
                    .padding(canvasPadding.dp)
            ) {
                drawCircle(
                    if (isSelected) GraphBarColors[selectedInd % GraphBarColors.size].copy(.2f) else Purple500.copy(
                        .1f
                    ), size.width * 1.1F / 2
                )
                sweepAngles.forEachIndexed { ind, sweepAngle ->
                    drawArc(
                        color = Color.DarkGray.copy(.2F),
                        startAngle = startAngle,
                        sweepAngle = (sweepAngle - 1) * barCompletion.value,
                        useCenter = false,
                        style = Stroke(
                            width = animateList[ind].value,
                            pathEffect = PathEffect.cornerPathEffect(2.dp.toPx())
                        ),
                        topLeft = Offset(10.dp.toPx(), 5.dp.toPx())
                    )

                    startAngle += sweepAngle
                }
                sweepAngles.forEachIndexed { ind, sweepAngle ->
                    drawArc(
                        color = GraphBarColors[ind % GraphBarColors.size].copy(.8F),
                        startAngle = startAngle,
                        sweepAngle = (sweepAngle - 1) * barCompletion.value,
                        useCenter = false,
                        style = Stroke(
                            width = animateList[ind].value,
                            pathEffect = PathEffect.cornerPathEffect(2.dp.toPx())
                        ),
                        topLeft = Offset(0F, 0F)
                    )

                    startAngle += sweepAngle
                }
            }
            Box(modifier = Modifier.size(DonutSize.dp), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier
                        .width(
                            DonutSize.dp / 1.5F
                        )
                        .height(DonutSize.dp / 1.5F)
                        .padding(start = 4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isSelected) names[selectedInd] else NoSelectionHeading,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6
                    )
                    AnimatedVisibility(visible = isSelected) {
                        val percentage = (proportions[selectedInd] * 100).roundToInt()
                        val percentText = if(percentage < 1) "< 1%" else "$percentage%"
                        Text(text = percentText, style = MaterialTheme.typography.h3)
                    }
                }
            }
        }
    }
}

fun Modifier.tapDonut(
    size: Size, onTap: (Size, Offset) -> Unit
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onTap = { coordinates ->
                onTap(size, coordinates)
            },
        )
    }
}

fun pointAngleConversion(width: Float, height: Float, x: Float, y: Float): Double {
    val deltaX = x - width / 2
    val deltaY = y - height / 2
    val rad = atan2(deltaY, deltaX)
    val degrees = rad * (180 / Math.PI)
    return if (degrees < 0) degrees + 360 else degrees
}

fun coordinateDistanceFromCenter(size: Size, coordinates: Offset): Double {
//    sqrt((x1 - x0)^2 + (y1 - y0)^2)
    return sqrt(
        (size.width / 2 - coordinates.x).toDouble()
            .pow(2.0) + (size.height / 2 - coordinates.y).toDouble().pow(2.0)
    )
}

@Preview
@Composable
fun PreviewDonutChart() {
    BadNutritionTheme {
        DonutChart()
    }
}