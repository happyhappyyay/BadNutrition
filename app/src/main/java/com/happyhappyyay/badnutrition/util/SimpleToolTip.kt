package com.happyhappyyay.badnutrition.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

enum class ToolTipDirection {
    Up,
    Down,
    Left,
    Right
}
const val MinToolTipWidth = 80
const val MaxToolTipWidth = 80
const val MinToolTipHeight = 45
const val MaxToolTipHeight = 75

@Composable
fun SimpleToolTip(
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    direction: ToolTipDirection = ToolTipDirection.Down,
    content: @Composable () -> Unit,


    ) {
//    val y = offset.y.value.coerceAtLeast(-16F).dp
//    val x = offset.x.value.coerceAtLeast(0F).dp
//    val correctOffset = DpOffset(x,y)
    val correctOffset = offset
//    val infiniteTransition = rememberInfiniteTransition()
//    val graphSelectedBarColor by infiniteTransition.animateColor(
//        initialValue = MaterialTheme.colors.secondary.copy(.75f),
//        targetValue = MaterialTheme.colors.secondary.copy(.35F),
//        animationSpec = infiniteRepeatable(
//            animation = tween(1200, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        )
//    )
    val correctForSp = with(LocalDensity.current) {1.sp.toDp()}
    val width = MinToolTipWidth * correctForSp
    val correctedWidth = if(direction == ToolTipDirection.Down) width else width + (width * .1F)
    val shape = when(direction) {
        ToolTipDirection.Left -> ToolTipRightShape()
        ToolTipDirection.Right -> ToolTipLeftShape()
        ToolTipDirection.Down -> ToolTipBottomShape()
        ToolTipDirection.Up -> ToolTipBottomShape()
    }
    val padding = when(direction) {
        ToolTipDirection.Left -> PaddingValues(end = width * .1f)
        ToolTipDirection.Right -> PaddingValues(start = width * .1f)
        ToolTipDirection.Down -> PaddingValues()
        ToolTipDirection.Up -> PaddingValues()
    }
    Box(
        modifier = modifier
            .offset(correctOffset.x, correctOffset.y)
            .background(Color.Transparent)
            .background(color = MaterialTheme.colors.background, shape = shape)
            .drawBehind {
                val outline = when(direction) {
                    ToolTipDirection.Left -> drawToolTipRightSidePointPath(this.size)
                    ToolTipDirection.Right -> drawToolTipLeftSidePointPath(this.size)
                    ToolTipDirection.Down -> drawToolTipBottomPointPath(this.size)
                    ToolTipDirection.Up -> drawToolTipBottomPointPath(this.size)
                }
                drawOutline(
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.cornerPathEffect(3.dp.toPx())
                    ),
                    outline = Outline.Generic(outline),
                    color = Color.Black
                )
            }

            .width(correctedWidth)
            .height(MinToolTipHeight * correctForSp)
            .padding(padding)
    )
    {
            content()
    }
}

@Preview
@Composable
fun PreviewSimpleToolTip() {
    BadNutritionTheme {
        SimpleToolTip(Modifier) {
            Text("dawg")
            Text("dawg")
        }
    }
}