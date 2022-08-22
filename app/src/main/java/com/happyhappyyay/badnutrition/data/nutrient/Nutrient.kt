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

fun calcNutrientValPercent(value: Int, minGoal : Int):Int {
    if(minGoal <= 0){
        return 100
    }
    return ((value/minGoal.toDouble())*100).toInt()
}
