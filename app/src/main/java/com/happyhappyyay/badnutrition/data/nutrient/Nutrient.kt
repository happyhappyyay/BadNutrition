package com.happyhappyyay.badnutrition.data.nutrient

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
