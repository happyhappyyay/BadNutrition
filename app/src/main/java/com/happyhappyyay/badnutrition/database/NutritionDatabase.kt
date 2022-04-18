package com.happyhappyyay.badnutrition.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.happyhappyyay.badnutrition.data.Day
import com.happyhappyyay.badnutrition.data.food.Food
import com.happyhappyyay.badnutrition.data.nutrient.NutrientInfo

@Database(entities = [NutrientInfo::class, Day::class, Food::class], version = 1, exportSchema = false)
@TypeConverters(ListNutrientValStringConverter::class, ListPortionStringConverter::class, GoalStringConverter::class)
abstract class NutritionDatabase : RoomDatabase() {

    abstract val nutrientInfoDao: NutrientDao
    abstract val dayDao: DayDao

    companion object {

        @Volatile
        private var INSTANCE: NutritionDatabase? = null

        fun getInstance(context: Context): NutritionDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NutritionDatabase::class.java,
                        "nutrition_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}