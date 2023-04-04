package com.happyhappyyay.badnutrition.data.nutrient

/**
 * Desired thresholds for nutrient intake
 *
 * @property min the minimum intake value
 * @property max the maximum intake value
 */
data class Goal(
    val min: Double = 0.0,
    val max: Double = -1.0,
)
