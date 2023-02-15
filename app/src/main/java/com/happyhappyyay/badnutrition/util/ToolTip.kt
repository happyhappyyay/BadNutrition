package com.happyhappyyay.badnutrition.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class ToolTipBottomShape() : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawToolTipBottomPointPath(size = size)
        )
    }
}

class ToolTipLeftShape() : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawToolTipLeftSidePointPath(size = size)
        )
    }
}

class ToolTipRightShape() : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = drawToolTipRightSidePointPath(size = size)
        )
    }
}

fun drawToolTipBottomPointPath(size: Size): Path {
    val rectT = Rect(Offset.Zero, size)

    return Path().apply {
        reset()
        with(rectT) {
            lineTo(topRight.x, topRight.y)
            lineTo(bottomRight.x, bottomRight.y * .9F)
            lineTo(bottomRight.x * .6F, bottomRight.y * .9F)
            lineTo(bottomCenter.x, bottomCenter.y - 1F)
            lineTo(bottomRight.x * .4F, bottomRight.y * .9F)
            lineTo(bottomLeft.x, bottomLeft.y * .9F)
            lineTo(topLeft.x, topLeft.y)
        }
        close()
    }
}

fun drawToolTipLeftSidePointPath(size: Size): Path {
    val (width, height) = size

    return Path().apply {
        reset()
        moveTo(width * .1F, 0F)
        lineTo(width, 0F)
        lineTo(width, height)
        lineTo(width * .1F, height)
        lineTo(width * .1f, height * .6F)
        lineTo(0F,height * .5F)
        lineTo(width * .1F, height * .4F)
        lineTo(width * .1F, 0F)
        close()
    }
}

fun drawToolTipRightSidePointPath(size: Size): Path {
    val (width, height) = size

    return Path().apply {
        reset()
        lineTo(width * .9F, 0F)
        lineTo(width * .9F, height * .4F)
        lineTo(width, height * .5F)
        lineTo(width * .9f, height * .6F)
        lineTo(width * .9f, height)
        lineTo(0F, height)
        lineTo(0F, 0F)
        close()
    }
}

fun drawToolTipLeftPointPath(size: Size): Path {
    val (width, height) = size

    return Path().apply {
        reset()
        moveTo(width * .1F, 0F)
        lineTo(width, 0F)
        lineTo(width, height*.95F)
        lineTo(width * .2F, height*.95F)
        lineTo(0F, height)
        lineTo(width * .1F, height * .8F)
        lineTo(width * .1F, 0F)
        close()
    }
}

fun drawToolTipRightPointPath(size: Size): Path {
    val (width, height) = size

    return Path().apply {
        reset()
        lineTo(width * .9F, 0F)
        lineTo(width * .9F, height*.8F)
        lineTo(width, height)
        lineTo(width * .9F,height * .95F)
        lineTo(0F,height*.95F)
        lineTo(0F,0F)
        close()
    }
}


class AppleShape() : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = drawApplePath(size = size)
        )
    }
}

fun drawApplePath(offset: Offset = Offset.Zero, size: Size, offsetCenter: Boolean = false): Path {
    val (width, height) = size
    var (x, y) = offset
    if (offsetCenter) {
        x = (x - width + x) / 2
        y = (y - height + y) / 2
    }
    val p = Path().apply {
        reset()

        moveTo(x + width * 0.50432336F, y + height * 0.9540431F)
        quadraticBezierTo(
            x + width * 0.30618492F,
            y + height * 1.1052939F,
            x + width * 0.13600734F,
            y + height * 0.8194067F
        )
        quadraticBezierTo(
            x + width * -0.13337415F,
            y + height * 0.30075628F,
            x + width * 0.08854868F,
            y + height * 0.163281F
        )
        quadraticBezierTo(
            x + width * 0.31047153F,
            y + height * 0.024316464F,
            x + width * 0.49443966F,
            y + height * 0.15706807F
        )
        quadraticBezierTo(
            x + width * 0.6958971F,
            y + height * 0.033973243F,
            x + width * 0.90033066F,
            y + height * 0.163281F
        )
        quadraticBezierTo(
            x + width * 1.1379057F,
            y + height * 0.31052938F,
            x + width * 0.8659645F,
            y + height * 0.8194067F
        )
        quadraticBezierTo(
            x + width * 0.7032455F,
            y + height * 1.1052939F,
            x + width * 0.50432336F,
            y + height * 0.9540431F
        )
//        quadraticBezierTo(x + width*.296F,y + height*1.188F,x + width*.128F,y + height*.820F)
//        quadraticBezierTo(x + width*-.122F,y + height*.361F,x + width*.081F,y + height*.163F)
//        quadraticBezierTo(x + width*.283F,y + height*-.031F,x + width*.486F,y + height*.157F)
//        quadraticBezierTo(x +width*.688F,y + height*-.031F,x + width*.892F,y + height*.163F)
//        quadraticBezierTo(x + width*1.093F,y + height*.358F,x + width*.858F,y + height*.820F)
//        quadraticBezierTo( x + width*.686F,y + height*1.185F,x + width*.493F,y + height*.867F)
        close()
    }
    return p

}

fun drawAppleStemPath(
    offset: Offset = Offset.Zero,
    size: Size,
    offsetCenter: Boolean = false
): Path {
    val (width, height) = size
    var (x, y) = offset
    if (offsetCenter) {
        x = (x - width + x) / 2
        y = (y - height + y) / 2
    }
    val q = Path().apply {
        reset()
//        moveTo(x + width*.486F, y + height*.157F)
//        quadraticBezierTo(x + width*.465F,y + height*.035F,x + width*.499F,y + height*.001F)
//        lineTo(x + width*.503F,y + height*.005F)
//        quadraticBezierTo(x + width*.488F,y + height*.103F,x + width*.502F,y + height*.143F)
        moveTo(x + width * 0.49443966F, y + height * 0.15706807F)
        quadraticBezierTo(
            x + width * 0.47483158F,
            y + height * 0.038161725F,
            x + width * 0.49965706F,
            y + height * 0.001454334F
        )
        lineTo(x + width * 0.5112186F, y + height * 0.0053287F)
        quadraticBezierTo(
            x + width * 0.5109614F,
            y + height * 0.10366492F,
            x + width * 0.5241886F,
            y + height * 0.14332752F
        )
        close()
    }
    return q

}
