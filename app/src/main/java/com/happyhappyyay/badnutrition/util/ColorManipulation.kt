package com.happyhappyyay.badnutrition.util

import androidx.compose.ui.graphics.Color

fun Color.adjustTransparency(alpha: Float): Color {
    var safeAlpha = alpha
    if (alpha > 1) safeAlpha = alpha / 100
    return Color(this.red, this.green, this.blue, safeAlpha)
}