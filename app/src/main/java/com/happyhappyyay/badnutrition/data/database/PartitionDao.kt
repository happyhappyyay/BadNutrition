package com.happyhappyyay.badnutrition.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.happyhappyyay.badnutrition.data.Day
import com.happyhappyyay.badnutrition.data.partition.Partition

@Dao
interface PartitionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(partition: Partition)

    @Update
    fun update(partition: Partition)
}
