package com.happyhappyyay.badnutrition.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
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
val leftBottomSpikeShape = CutCornerShape(topStart = 1000F)
val leftTopSpikeShape = CutCornerShape(bottomStart = 1000F)
val halfRoundedShapeTop = RoundedCornerShape(10.dp,10.dp,0.dp,0.dp)
val roundedShapeTop = RoundedCornerShape(3.5.dp,3.5.dp,0.dp,0.dp)
val smallRoundShape = RoundedCornerShape(2.dp)
val roundedLeftShape = RoundedCornerShape(10.dp,0.dp,0.dp,10.dp)
val roundedRightShape = RoundedCornerShape(0.dp,10.dp,10.dp,0.dp)
val chipRoundedShape = RoundedCornerShape(8.dp)
