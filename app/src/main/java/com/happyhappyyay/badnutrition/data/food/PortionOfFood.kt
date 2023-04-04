package com.happyhappyyay.badnutrition.data.food

import androidx.room.Embedded
import androidx.room.Relation
import com.happyhappyyay.badnutrition.data.partition.Partition

data class PortionOfFood(
    @Embedded
    val portion: Portion,
    @Relation(parentColumn = "foodId", entityColumn = "id")
    val food: Food,
    @Relation(parentColumn = "partitionId", entityColumn = "id")
    val partition: Partition
)