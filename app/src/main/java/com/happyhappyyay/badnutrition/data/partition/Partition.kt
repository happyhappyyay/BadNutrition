package com.happyhappyyay.badnutrition.data.partition

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.happyhappyyay.badnutrition.data.nutrient.NutrientValue

/**
 * A Room datum for a partition
 *
 *
 * @property id the id for the partition
 * @property name the name of the partition
 */
@Entity(tableName = "partitions")
data class Partition(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
)