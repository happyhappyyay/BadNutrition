package com.happyhappyyay.badnutrition.charts

import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.GraphBarColors
import com.happyhappyyay.badnutrition.util.LegendItem
import com.happyhappyyay.badnutrition.util.SimpleLegend
import com.happyhappyyay.badnutrition.util.SimpleToolTip
import com.happyhappyyay.badnutrition.util.MinToolTipWidth
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.*

//https://www.droidcon.com/2022/01/20/interacting-with-composable-canvas/
const val BAR_DISTANCE = 40F
const val SCATTER_DISTANCE = 5F
const val LINE_DISTANCE = 25F


//75F - bar
//35F - scatter
//20F - line
const val ellipses = "..."

val gList = arrayListOf(
    GraphData("pAAty", 100F), GraphData("Rooice", 20F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        60F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("apples", 70F),
    GraphData("snaekies", 60F),
    GraphData("Sugar", 20F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 22F),
    GraphData("Protein", 44F),
    GraphData("Carbohydrates", 1F),
    GraphData("Sodium", 68F),
    GraphData("Sugar", 75F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        49F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 23F),
    GraphData("Ash", 24F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 23F),
    GraphData("Calcium", 24F),
    GraphData("Manganese", 25F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil", 24F
    ),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Carbohydrates", 23F),
    GraphData("Sodium", 22F),
    GraphData("Sugar", 21F),
    GraphData("Saturated Fat", 20F),
    GraphData("Unsaturated Fat", 21F),
    GraphData("Calories", 22F),
    GraphData("Tocopherol", 23F),
    GraphData("Ash", 24F),
    GraphData("Calcium", 25F),
    GraphData("Manganese", 26F),
    GraphData("Protein", 27F),
    GraphData("Carbohydrates", 28F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Sodium", 29F),
    GraphData("Sugar", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 30F),
    GraphData("Ash", 29F),
    GraphData("Ash", 28F),
    GraphData("Ash", 27F),
    GraphData("Ash", 26F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 19F),
    GraphData("Ash", 18F),
    GraphData("Ash", 17F),
    GraphData("Ash", 16F),
    GraphData("Ash", 15F),
    GraphData("Ash", 14F),
    GraphData("Ash", 13F),
    GraphData("Ash", 12F),
    GraphData("Ash", 11F),
    GraphData("Ash", 10F),
    GraphData("Ash", 9F),
    GraphData("Ash", 8F),
    GraphData("Ash", 7F),
    GraphData("Ash", 6F),
    GraphData("Ash", 6F),
    GraphData("Ash", 5F),
    GraphData("Ash", 4F),
    GraphData("Ash", 3F),
    GraphData("Ash", 2F),
    GraphData("Ash", 1F),
    GraphData("Ash", 0F),
    GraphData("MEAT", 100F),
    GraphData("MEAT", 100F),
    GraphData("MEAT", 100F)
)
val hList = arrayListOf(
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        49F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        49F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
        GraphData("pAAty", 100F), GraphData("Rooice", 20F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
60F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("apples", 70F),
GraphData("snaekies", 60F),
GraphData("Sugar", 20F),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 49F),
GraphData("Calcium", 100F),
GraphData("Manganese", 22F),
GraphData("Protein", 44F),
GraphData("Carbohydrates", 1F),
GraphData("Sodium", 68F),
GraphData("Sugar", 75F),
GraphData("Saturated Fat", 12F),
GraphData("Unsaturated Fat", 88F),
GraphData("Calories", 94F),
GraphData("Tocopherol", 73F),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 49F),
GraphData("Calcium", 100F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData("Saturated Fat", 40F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData(
"Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
49F
),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Calcium", 100F),
GraphData("Manganese", 90F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 49F),
GraphData("Calcium", 100F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 49F),
GraphData("Ash", 49F),
GraphData("Ash", 49F),
GraphData("Ash", 49F),
GraphData("Ash", 49F),
GraphData("Ash", 20F),
GraphData("Ash", 21F),
GraphData("Ash", 22F),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 23F),
GraphData("Ash", 24F),
GraphData("Ash", 25F),
GraphData("Ash", 24F),
GraphData("Ash", 23F),
GraphData("Ash", 22F),
GraphData("Ash", 21F),
GraphData("Ash", 20F),
GraphData("Ash", 21F),
GraphData("Ash", 22F),
GraphData("Ash", 23F),
GraphData("Calcium", 24F),
GraphData("Manganese", 25F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil", 24F
),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Carbohydrates", 23F),
GraphData("Sodium", 22F),
GraphData("Sugar", 21F),
GraphData("Saturated Fat", 20F),
GraphData("Unsaturated Fat", 21F),
GraphData("Calories", 22F),
GraphData("Tocopherol", 23F),
GraphData("Ash", 24F),
GraphData("Calcium", 25F),
GraphData("Manganese", 26F),
GraphData("Protein", 27F),
GraphData("Carbohydrates", 28F),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Sodium", 29F),
GraphData("Sugar", 30F),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Saturated Fat", 12F),
GraphData("Unsaturated Fat", 88F),
GraphData("Calories", 94F),
GraphData("Tocopherol", 73F),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 49F),
GraphData("Calcium", 100F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 49F),
GraphData("Calcium", 100F),
GraphData("Manganese", 90F),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 49F),
GraphData("Calcium", 100F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Ash", 48F),
GraphData("Calcium", 9F),
GraphData("Manganese", 90F),
GraphData("Protein", 80F),
GraphData("Carbohydrates", 70F),
GraphData("Sodium", 60F),
GraphData("Sugar", 50F),
GraphData(
"Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
80F
),
GraphData("Saturated Fat", 40F),
GraphData("Unsaturated Fat", 30F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Calories", 20F),
GraphData("Tocopherol", 10F),
GraphData("Ash", 49F),
GraphData("Ash", 49F),
GraphData("Ash", 49F),
GraphData("Ash", 49F),
GraphData("Ash", 30F),
GraphData("Ash", 29F),
GraphData("Ash", 28F),
GraphData("Ash", 27F),
GraphData("Ash", 26F),
GraphData("Ash", 25F),
GraphData("Ash", 24F),
GraphData("Ash", 23F),
GraphData("Ash", 22F),
GraphData("Ash", 21F),
GraphData("Ash", 20F),
GraphData("Ash", 19F),
GraphData("Ash", 18F),
GraphData("Ash", 17F),
GraphData("Ash", 16F),
GraphData("Ash", 15F),
GraphData("Ash", 14F),
GraphData("Ash", 13F),
GraphData("Ash", 12F),
GraphData("Ash", 11F),
GraphData("Ash", 10F),
GraphData("Ash", 9F),
GraphData("Ash", 8F),
GraphData("Ash", 7F),
GraphData("Ash", 6F),
GraphData("Ash", 6F),
GraphData("Ash", 5F),
GraphData("Ash", 4F),
GraphData("Ash", 3F),
GraphData("Ash", 2F),
GraphData("Ash", 1F),
GraphData("Ash", 0F),
    GraphData("pAAty", 100F), GraphData("Rooice", 20F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        60F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("apples", 70F),
    GraphData("snaekies", 60F),
    GraphData("Sugar", 20F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 22F),
    GraphData("Protein", 44F),
    GraphData("Carbohydrates", 1F),
    GraphData("Sodium", 68F),
    GraphData("Sugar", 75F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        49F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 23F),
    GraphData("Ash", 24F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 23F),
    GraphData("Calcium", 24F),
    GraphData("Manganese", 25F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil", 24F
    ),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Carbohydrates", 23F),
    GraphData("Sodium", 22F),
    GraphData("Sugar", 21F),
    GraphData("Saturated Fat", 20F),
    GraphData("Unsaturated Fat", 21F),
    GraphData("Calories", 22F),
    GraphData("Tocopherol", 23F),
    GraphData("Ash", 24F),
    GraphData("Calcium", 25F),
    GraphData("Manganese", 26F),
    GraphData("Protein", 27F),
    GraphData("Carbohydrates", 28F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Sodium", 29F),
    GraphData("Sugar", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 30F),
    GraphData("Ash", 29F),
    GraphData("Ash", 28F),
    GraphData("Ash", 27F),
    GraphData("Ash", 26F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 19F),
    GraphData("Ash", 18F),
    GraphData("Ash", 17F),
    GraphData("Ash", 16F),
    GraphData("Ash", 15F),
    GraphData("Ash", 14F),
    GraphData("Ash", 13F),
    GraphData("Ash", 12F),
    GraphData("Ash", 11F),
    GraphData("Ash", 10F),
    GraphData("Ash", 9F),
    GraphData("Ash", 8F),
    GraphData("Ash", 7F),
    GraphData("Ash", 6F),
    GraphData("Ash", 6F),
    GraphData("Ash", 5F),
    GraphData("Ash", 4F),
    GraphData("Ash", 3F),
    GraphData("Ash", 2F),
    GraphData("Ash", 1F),
    GraphData("Ash", 0F),
)
val values = arrayOf(
    "100", "80", "60", "40", "20", "0%"
)

val condensedValues = arrayOf(
    "100", "50", "0%"
)
val legendaryItems = arrayOf(
    LegendItem(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        Color.Red
    ),
    LegendItem(
        "Carbohydrates", Color.Magenta
    ),
    LegendItem("Calories", Color.Black),
    LegendItem("Fat", Color.Blue),
    LegendItem("Onion Rings", Color.Green)
)

suspend fun ScrollState.setScrolling(value: Boolean) {
    scroll(scrollPriority = MutatePriority.PreventUserInput) {
        when (value) {
            true -> Unit
            else -> awaitCancellation()
        }
    }
}

fun Modifier.zoom(
    onZoom: (zoom: Float) -> Unit
): Modifier {
    return this
        .pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitFirstDown(false)
                    do {
                        val event = awaitPointerEvent()
                        val zoom = event.calculateZoom()
                        if (zoom != 1F) {
                            onZoom(zoom)
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
        }
}

fun Modifier.doubleTap(
    onDoubleTap: () -> Unit
): Modifier {
    return this
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    onDoubleTap()
                    Log.d("GRAPHATTEMPT2", "double tap")
                },
            )
        }
}

fun Modifier.tapGestures(
    interactionSource: MutableInteractionSource,
    onTap: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onDoubleTap: () -> Unit = {},
): Modifier {
    return this
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    val press = PressInteraction.Press(offset)
                    interactionSource.emit(press)
                    tryAwaitRelease()
                    interactionSource.emit(PressInteraction.Release(press))
                },
                onLongPress = {
                    onLongPress()
                },
                onDoubleTap = {
                    onDoubleTap()
                    Log.d("GRAPHATTEMPT2", "double tap")
                },
                onTap = {
                    onTap()
                }
            )
        }
}

fun Modifier.longPressDrag(
    onSelect: (Offset) -> Unit
): Modifier {
    return this
//            needs to cancel out detectTapGestures later on the modifier chain to override simultaneous long press
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(onDragStart = { offset -> onSelect(offset) }) { change, dragAmount ->
                onSelect(Offset(change.position.x, change.position.y))
            }
        }
}

fun Modifier.tapOrPress(
    scrollableState: ScrollState,
    onSelect: (offset: Offset) -> Unit,
    onDoubleTap: () -> Unit = {},
    onZoom: (zoom: Float) -> Unit = {}
): Modifier = composed {

    val rememberCoroutineScope = rememberCoroutineScope()
    var rememberZoom by remember { mutableStateOf(1f) }

    this
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    onDoubleTap()
                    Log.d("GRAPHATTEMPT2", "double tap")
                },
            )
        }
        .animateContentSize()
        .pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitFirstDown()
                    do {
                        val event = awaitPointerEvent()
                        rememberZoom = event.calculateZoom()
                        if (rememberZoom != 1F) {
                            rememberCoroutineScope.launch {
                                scrollableState.setScrolling(false)
                            }
                            onZoom(rememberZoom)
                            rememberCoroutineScope.launch {
                                scrollableState.setScrolling(true)
                            }
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
        }
        .animateContentSize()
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(onDragStart = { offset -> onSelect(offset) }) { change, dragAmount ->
                Log.d("GRAPHATTEMPT2", "longPress")
                rememberCoroutineScope.launch {
                    scrollableState.scrollTo(scrollableState.value + dragAmount.x.toInt() * 10)
                }
                onSelect(Offset(change.position.x, change.position.y))
            }
        }
}

@Composable
fun GraphAttempt2(
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
        GraphType.Bar -> BAR_DISTANCE.dp
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
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp * .9F
        val estimatedWidth = with(LocalDensity.current) {
            ((2 * distance) * list.size + distance).toDp()
        }
        val width = if (screenWidth > estimatedWidth) screenWidth else estimatedWidth
        val topPadding = 10.dp
        val bottomPadding = if (graphType == GraphType.Bar) 10.dp else 30.dp

        Column(
            modifier = modifier.background(color = graphBackgroundColor),
        ) {
            title()
            Row(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.width(50.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                    ) {
                        val colMod = if (constrainYText) Modifier.padding(
                            top = 13.dp, bottom = 11.dp
                        ) else Modifier.padding(
                            top = topPadding, bottom = bottomPadding - topPadding
                        )
                        Column(
                            modifier = colMod.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            values.forEach { value ->
                                Text(
                                    text = value,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = if (constrainYText) MaterialTheme.typography.caption.copy(
                                        fontSize = 11.sp
                                    ) else MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onBackground
                                )
                            }
                        }
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        Spacer(modifier = Modifier.height(topPadding))
                        Row(
                            modifier = Modifier
                                .horizontalScroll(scrollState)
                                .width(width)
                                .weight(1f)
                        ) {
                            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            top = topPadding, bottom = bottomPadding
                                        )
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colorStops = colorStops,
                                            )
                                        )
                                )
                                val heightDp = this.maxHeight
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
                                val min = if (constrainYText) 30 else 20
                                val barMinimum =
                                    heightPx - (heightPx - 2 * verticalPadding.first) * (min / 100F) - bottomPadPx
                                val barAreas = list.mapIndexed { index, item ->
                                    BarArea(
                                        index = index,
                                        data = item,
                                        xStart = distance.times(index) + distance.times(
                                            index
                                        ) - distance.div(
                                            2
                                        ) + distance.times(1.5F),
                                        xEnd = distance.times(index) + distance.times(index) + distance.div(
                                            2
                                        ) + distance.times(1.5F),
                                        yStart = (heightPx - (heightPx - (topPadPx + bottomPadPx)) * (item.value / 100F)) - bottomPadPx,
                                        yEnd = 0F
                                    )
                                }
                                val selectedBar =
                                    if (selectedBarNum > -1 && barAreas.isNotEmpty()) barAreas[selectedBarNum] else null
//                            val selectedBar by remember(selectedPosition, barAreas) {
//                                derivedStateOf {
////                                    barAreas.find { it.xStart < selectedPosition.x && selectedPosition.x < it.xEnd && it.yStart <= selectedPosition.y }
////                                }
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
                                    key(heightPx) {
                                        Canvas(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .tapOrPress(scrollableState = rememberScrollState(),
                                                    onSelect = { offset ->
                                                        val (x: Float, y: Float) = offset
                                                        if (graphType == GraphType.Bar) {
                                                            onSelected(barAreas.find { bar -> bar.xStart < x && x < bar.xEnd && (bar.yStart <= y || barMinimum <= y) && y < heightPx - bottomPadPx })
                                                        } else {
                                                            onSelected(barAreas.find { bar -> bar.xStart - distance < x && x < bar.xStart + distance && bar.yStart - distance <= y && bar.yStart + distance >= y })
                                                        }
                                                    })
                                        ) {
                                            val startOffset = Offset(0F, size.height - bottomPadPx)
                                            val endOffset =
                                                Offset(size.width, size.height - bottomPadPx)
                                            drawLine(
                                                Color.Black, startOffset, endOffset, strokeWidth
                                            )
                                            if (graphType == GraphType.Bar) {
                                                drawBarGraph(
                                                    barAreas,
                                                    selectedBarNum,
                                                    selectedColor = graphSelectedBarColor,
                                                    verticalPadding = verticalPadding
                                                )
                                            } else {
                                                drawPointGraph(
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
                                if (selectedBar != null) {
                                    var x = with(LocalDensity.current) { selectedBar.xEnd.toDp() }
                                    var y = with(LocalDensity.current) {
                                        selectedBar.yStart.plus(10).toDp()
                                    }
                                    if (x + MinToolTipWidth.dp > width) {
                                        x = width - MinToolTipWidth.dp - 10.dp
                                    }
//                                    if (y + simpleToolTipHeight.dp > heightDp) {
//                                        y =
//                                            heightDp - (simpleToolTipHeight.dp)
//                                    }
                                    SimpleToolTip {
                                        Column(
                                            modifier = Modifier
                                                .padding(
                                                    vertical = 4.dp, horizontal = 8.dp
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
fun PreviewGraphAttempt2() {
    BadNutritionTheme {
        GraphAttempt2(selectedBarNum = 3, onSelected = {})
    }
}

fun DrawScope.drawBarGraph(
    barAreas: List<BarArea>,
    selectedBarInd: Int,
    selectedColor: Color,
    verticalPadding: Pair<Float, Float>
) {
    val (topPadPx, bottomPadPx) = verticalPadding
    val barTextPadding = BAR_DISTANCE.div(5f)
    val barWidth = if (barAreas.isEmpty()) 0F else barAreas[0].xEnd - barAreas[0].xStart
    barAreas.forEach { bar ->
        val distFromGraphBottom = size.height - bar.yStart - topPadPx
        if (bar.index == selectedBarInd) {
            drawRoundRect(
                color = selectedColor,
                topLeft = Offset(
                    bar.xStart - 10, bar.yStart - 10
                ),
                size = Size(barWidth + 20, distFromGraphBottom + 13),
                cornerRadius = CornerRadius(10F, 10F)

            )
        } else {
            if (distFromGraphBottom > 0) {
                drawRoundRect(
                    color = Color.Black.copy(.2f),
                    topLeft = Offset(
                        bar.xStart + 10, bar.yStart + 10
                    ),
                    size = Size(barWidth, distFromGraphBottom - 7),
                    cornerRadius = CornerRadius(7F, 7F)
                )
            }
        }
        drawRoundRect(
            color = GraphBarColors[bar.index % GraphBarColors.size],
            topLeft = Offset(bar.xStart, bar.yStart),
            size = Size(barWidth, distFromGraphBottom - 2),
            cornerRadius = CornerRadius(7F, 7F)
        )
    }

    val paint = Paint()
    paint.textSize = 12.sp.toPx()
    val selectedPaint = Paint()
    selectedPaint.typeface = Typeface.DEFAULT_BOLD
    selectedPaint.textSize = 14.sp.toPx()
    selectedPaint.color = selectedColor.copy(1f).toArgb()
    clipRect(0F, size.height, size.width, topPadPx) {
        translate {
            rotate(270F, Offset(0F, 0F)) {
                drawIntoCanvas {
                    barAreas.forEach { bar ->
                        val barName = bar.data.name
//                    val textMaxHeight = size.height -(size.height*.1)- 2 * verticalPadding - barTextPadding
//                    var correctedBarName = barName
//                    val textLengthPx = paint.measureText(barName)
//                    if(textLengthPx > textMaxHeight){
//                        val ellipsesLen = paint.measureText(ellipses)
//                        val sb = StringBuilder(barName)
//                        if(lengthPrev == 0){
//                            lengthPrev = sb.length
//                        }
//                        sb.setLength(lengthPrev + 5)
//                        while(paint.measureText(sb.toString()) + ellipsesLen > textMaxHeight ){
//                            sb.setLength(sb.length-1)
//                        }
//                        sb.append(ellipses)
//                        correctedBarName = sb.toString()
//                        if(sb.length > lengthPrev){
//                            lengthPrev = sb.length
//                        }
//                    }
                        it.nativeCanvas.drawText(
                            barName,
                            -size.height + bottomPadPx + barTextPadding,
                            bar.xStart - barTextPadding,
                            if (selectedBarInd == bar.index) selectedPaint
                            else paint
                        )
                    }
                }
            }
        }
    }

    drawIntoCanvas {
        it.nativeCanvas.drawText("$1", 200F, 200F, Paint().apply { textSize = 50F })
    }
}

fun DrawScope.drawPointGraph(
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
            val nextOffset = Offset(nextBar.xStart, nextBar.yStart)
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
                    Color.Black, start = curOffset, end = nextOffset, strokeWidth = 1.5.dp.toPx()
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
                                if (selectedBarInd == bar.index) selectedPaint
                                else paint
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
                    "${i + 1}", xOffset, size.height - xTickMarkSize + 1.dp.toPx(), paint
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
