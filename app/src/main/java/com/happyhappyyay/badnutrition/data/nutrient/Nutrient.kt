package com.happyhappyyay.badnutrition.data.nutrient

import kotlin.math.roundToInt

/**
 * Data class that combines the stored {@link Room}
 * {@link com.happyhappyyay.badnutrition.data.nutrients.NutrientInfo}
 * and
 * {@link com.happyhappyyay.badnutrition.data.nutrients.NutrientValue}.
 *
 */
data class Nutrient(
    val name: String,
    val value: Int,
    val measurement: String,
    val goal: Goal
)

fun calcNutrientValPercent(value: Double, minGoal : Double):Float {
    if(minGoal <= 0){
        return 100F
    }
    return ((value/minGoal)*100).toFloat()
}
