package com.happyhappyyay.badnutrition.data.database

import androidx.room.TypeConverter
import com.happyhappyyay.badnutrition.data.food.Portion
import com.happyhappyyay.badnutrition.data.nutrient.Goal
import com.happyhappyyay.badnutrition.data.nutrient.NutrientValue
import java.lang.Double.parseDouble
import java.lang.Long.parseLong

const val UNIT_DELIMITER = "␟"
const val GROUP_DELIMITER = "␝"

//class HashMapStringConverter {
//    companion object {
//        @TypeConverter
//        @JvmStatic
//        fun fromHashMap(map: HashMap<String, Int>): String {
//            val sb = StringBuilder()
//            map.entries.forEach { a ->
//                val s = "${a.key}$UNIT_DELIMITER${a.value}$UNIT_DELIMITER"
//                sb.append(s)
//            }
//            sb.setLength(sb.length-1)
//            return sb.toString()
//        }
//
//        @TypeConverter
//        @JvmStatic
//        fun fromString(string: String): HashMap<String, Int> {
//            val map = HashMap<String, Int>()
//            val mapString = string.split(DELIMITER)
//            for(i in mapString.indices step 2){
//                map.put(mapString[i], parseInt(mapString[i+1]))
//            }
//            return map
//        }
//    }
//}

class GoalStringConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromGoal(goal: Goal): String {
            return "${goal.min},${goal.max}"
        }

        @TypeConverter
        @JvmStatic
        fun fromString(string: String): Goal {
            val goalValues = string.split(",")
            return Goal(goalValues[0].toDouble(), goalValues[1].toDouble())
        }
    }
}

class ListNutrientValStringConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromListNutrientVal(nutrients: List<NutrientValue>): String {
            val sb = StringBuilder()
            if (nutrients.isNotEmpty()) {
                nutrients.forEach { nutrient ->
                    sb.append("${nutrient.nameId}:${nutrient.value},")
                }
                sb.setLength(sb.length - 1)
            }
            return sb.toString()
        }

        @TypeConverter
        @JvmStatic
        fun fromString(nutrients: String): List<NutrientValue> {
            val nutrientList = ArrayList<NutrientValue>()
            if (nutrients.isNotEmpty()) {
                nutrients.split(',').forEach { nutrient ->
                    val nutrientVals = nutrient.split(':')
                    nutrientList.add(
                        NutrientValue(
                            parseLong(nutrientVals[0]),
                            parseDouble(nutrientVals[1])
                        )
                    )
                }
            }
            return nutrientList
        }
    }
}

class ListPortionStringConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromListPortion(portions: List<Portion>): String {
            val sb = StringBuilder()
            if (portions.isNotEmpty()) {
                portions.forEach { portion ->
                    sb.append("${portion.id}$UNIT_DELIMITER${portion.foodId}$UNIT_DELIMITER${portion.amount}$UNIT_DELIMITER${portion.partitionId}$UNIT_DELIMITER${portion.dateMs}$GROUP_DELIMITER")
                }
                sb.setLength(sb.length - 1)
            }
            return sb.toString()
        }

        @TypeConverter
        @JvmStatic
        fun fromString(portionString: String): List<Portion> {
            val portions = ArrayList<Portion>()
            if (portionString.isNotEmpty()) {
                portionString.split(GROUP_DELIMITER).forEach { portion ->
                    val portionArr = portion.split(UNIT_DELIMITER)
                    portions.add(
                        Portion(
                            portionArr[0].toLong(),
                            portionArr[1].toLong(),
                            portionArr[2].toDouble(),
                            portionArr[3].toLong(),
                            portionArr[4].toLong()
                        )
                    )
                }
            }
            return portions
        }
    }
}

//class ListFoodStringConverter {
//    companion object {
//        @TypeConverter
//        @JvmStatic
//        fun fromListFood(foods: List<Food>): String {
//            val sb = StringBuilder()
//            foods.forEach{ food ->
//                val nutrients = ListNutrientValStringConverter.fromListNutrientVal(food.nutrients)
//                sb.append(
//                    "${food.foodId}$UNIT_DELIMITER${food.name}$UNIT_DELIMITER$nutrients$UNIT_DELIMITER${food.mealPartition}$GROUP_DELIMITER"
//                )
//            }
//            if(sb.isNotEmpty()) {
//                sb.setLength(sb.length - 1)
//            }
//            return sb.toString()
//        }
//
//        @TypeConverter
//        @JvmStatic
//        fun fromString(foodsString: String): List<Food> {
//            val foods = ArrayList<Food>()
//            if(foodsString.isNotEmpty()){
//                foodsString.split(GROUP_DELIMITER).forEach { food ->
//                    val foodInfo = food.split(UNIT_DELIMITER)
//                    val nutrientList = ListNutrientValStringConverter.fromString(foodInfo[2])
//                    foods.add(
//                        Food(
//                            foodInfo[0].toLong(),
//                            foodInfo[1],
//                            nutrientList,
//                            foodInfo[3].toInt()
//                        )
//                    )
//                }
//            }
//            return foods
//        }
//    }
//}