package com.happyhappyyay.badnutrition.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.happyhappyyay.badnutrition.data.Day

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(day: Day)

    @Update
    fun update(day: Day)
}
