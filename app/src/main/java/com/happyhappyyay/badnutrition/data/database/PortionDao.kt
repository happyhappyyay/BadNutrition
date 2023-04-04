package com.happyhappyyay.badnutrition.data.database

import androidx.room.*
import com.happyhappyyay.badnutrition.data.Day
import com.happyhappyyay.badnutrition.data.food.PortionOfFood
import kotlinx.coroutines.flow.Flow

@Dao
interface PortionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(day: Day)

//    @Query("""
//    SELECT * FROM portions
//    INNER JOIN foods as food_ ON portions.foodId = food_.id
//""")
    @Transaction
    @Query("SELECT * FROM portions")
    fun getPortionsOfFood(): Flow<List<PortionOfFood>>

    @Update
    fun update(day: Day)
}
