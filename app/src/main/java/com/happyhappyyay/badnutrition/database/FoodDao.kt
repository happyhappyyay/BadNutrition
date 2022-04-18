package com.happyhappyyay.badnutrition.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Update
import com.happyhappyyay.badnutrition.data.food.Food

interface FoodDao {
    @Insert(onConflict = REPLACE)
    fun insert(food: Food)

    @Update
    fun update(food: Food)
}
