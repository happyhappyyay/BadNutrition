package com.happyhappyyay.badnutrition.data.nutrient
/**
 * Data class for the different values a nutrient can take.
 *
 * Specific day values for a nutrient.
 *
 * @property nameId the associated Nutrient Info item.
 * @property value the quantity of the nutrient value.
 *
 */
data class NutrientValue(
    val nameId: Long,
    val value: Double,
)