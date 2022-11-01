package com.happyhappyyay.badnutrition.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(0.dp),
)

val halfRoundShapeRight = RoundedCornerShape(0.dp,10.dp)
val halfRoundedShapeLeft = RoundedCornerShape(10.dp,0.dp,0.dp,10.dp)
val halfRoundedShapeRightDeep = RoundedCornerShape(0.dp,20.dp,20.dp,0.dp)
val halfRoundedShapeLeftDeep = RoundedCornerShape(20.dp,0.dp,0.dp,20.dp)
val binderTabShapeRight = RoundedCornerShape(0.dp,20.dp)