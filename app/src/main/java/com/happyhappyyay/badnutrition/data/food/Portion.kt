package com.happyhappyyay.badnutrition.data.food

/**
 * Portion size of a specific food
 *
 * @property nameId the associated Food item
 * @property amount the amount of the Food
 * @property partition meal associated with Portion
 */
data class Portion(
    val nameId: Long,
    val amount: Double,
    val partition: Int
    )