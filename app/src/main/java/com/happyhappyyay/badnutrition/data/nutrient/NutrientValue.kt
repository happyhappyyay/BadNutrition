package com.happyhappyyay.badnutrition.data.nutrient
/**
 * Data class for the different values a nutrient can take.
 *
 * Specific day values for a nutrient.
 *
 * @property name the name of the nutrient value.
 * @property value the value of the nutrient value.
 *
 */
data class NutrientValue(
    val nameId: Int,
    val value: Int,
)