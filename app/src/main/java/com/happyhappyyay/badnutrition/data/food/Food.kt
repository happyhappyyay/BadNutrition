package com.happyhappyyay.badnutrition.data.food

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.happyhappyyay.badnutrition.data.nutrient.NutrientValue

/**
 * A Room datum for a food
 *
 * @property id the id for the food
 * @property name the name of the food
 * @property nutrients the nutrients associated with the food
 */
@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val nutrients: List<NutrientValue>,
)