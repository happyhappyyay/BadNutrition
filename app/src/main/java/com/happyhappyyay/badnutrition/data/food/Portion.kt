package com.happyhappyyay.badnutrition.data.food

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Portion of a specific food
 *
 * @property id the Portion id
 * @property foodId the associated Food item
 * @property amount the amount of the Food
 * @property partitionId partition associated with Portion
 * @property dateMs the date, in milliseconds, of this Portion
 */
@Entity (tableName = "portions")
data class Portion(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val foodId: Long,
    val amount: Double,
    val partitionId: Long,
    val dateMs: Long,
    )