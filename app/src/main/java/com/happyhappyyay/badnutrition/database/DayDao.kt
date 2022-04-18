package com.happyhappyyay.badnutrition.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Update
import com.happyhappyyay.badnutrition.data.Day

@Dao
interface DayDao {

    @Insert(onConflict = REPLACE)
    fun insert(day: Day)

    @Update
    fun update(day: Day)
}
