package com.happyhappyyay.badnutrition.data.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.happyhappyyay.badnutrition.data.food.Food

interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(food: Food)

    @Update
    fun update(food: Food)
}
