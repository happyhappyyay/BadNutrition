package com.happyhappyyay.badnutrition.data.nutrient

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for the default nutrient information.
 *
 * Stores general information about a nutrient.
 *
 * @property nutrientInfoId the nutrient information id.
 * @property name the name of the nutrient.
 * @property measurement the measurement unit of the nutrient.
 * @property order the nutrient's order in a list of nutrients.
 * @property defaultGoal the nutrient information's standard nutrient goal.
 * @property goal the nutrient information's current goal.
 */
@Entity(tableName = "nutrient_info")
data class NutrientInfo(
    @PrimaryKey(autoGenerate = true)
    val nutrientInfoId: Long,
    val name: String,
    val measurement: String,
    val order: Int,
    val defaultGoal: Goal,
    val goal: Goal = defaultGoal
)
