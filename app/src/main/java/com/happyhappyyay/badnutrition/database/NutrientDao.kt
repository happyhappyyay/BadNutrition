package com.happyhappyyay.badnutrition.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Update
import com.happyhappyyay.badnutrition.data.nutrient.NutrientInfo

@Dao
interface NutrientDao {

    @Insert(onConflict = REPLACE)
    fun insert(nutrient: NutrientInfo)

    @Update
    fun update(nutrient: NutrientInfo)

}
