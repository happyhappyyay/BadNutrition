package com.happyhappyyay.badnutrition.data.nutrient

/**
 * Desired thresholds for nutrient intake
 *
 * @property min the minimum intake value
 * @property max the maximum intake value
 */
data class Goal(
    val min: Int = 0,
    val max: Int = -1,
)
