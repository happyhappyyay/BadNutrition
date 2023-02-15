package com.happyhappyyay.badnutrition.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.happyhappyyay.badnutrition.data.nutrient.NutrientInfo

@Dao
interface NutrientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nutrient: NutrientInfo)

    @Update
    fun update(nutrient: NutrientInfo)

}
