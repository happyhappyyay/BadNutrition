package com.happyhappyyay.badnutrition

import com.happyhappyyay.badnutrition.data.database.*
import com.happyhappyyay.badnutrition.data.food.Portion
import com.happyhappyyay.badnutrition.data.nutrient.Goal
import com.happyhappyyay.badnutrition.data.nutrient.NutrientValue
import com.happyhappyyay.badnutrition.database.*
import org.junit.Assert
import org.junit.Test

class TypeConverterUnitTest {

    private val nutrients = listOf(
        NutrientValue(0,1),
        NutrientValue(1,2),
        NutrientValue(2,3),
        NutrientValue(322,455),
        NutrientValue(46666,5),
        NutrientValue(5,65),
        NutrientValue(-1,0)
    )

//    private val foods = listOf(
//        Food(0,"apple", nutrients, 0),
//        Food(555433,"banana", nutrients, 55),
//        Food(3435,"cactus", nutrients, 1),
//        Food(11111,"deli meat!", nutrients, 1),
//        Food(55,"eucalyptus", nutrients, 522),
//    )

    private val portions = listOf(
        Portion(0,155.32, 33),
        Portion(0,233.1, 1),
        Portion(5,1.0, 3),
        Portion(11111111,1554.55, 9999),
        Portion(219,43.0, 332222),
        )

    @Test
    fun listNutrientValueToStringValid() {
        val expected = "0:1,1:2,2:3,322:455,46666:5,5:65,-1:0"
        val nutrients = this.nutrients

        val actual = ListNutrientValStringConverter.fromListNutrientVal(nutrients)

        Assert.assertEquals(expected,actual)
    }

    @Test
    fun listNutrientValueToStringEmpty() {
        val expected = ""
        val nutrients = ArrayList<NutrientValue>()

        val actual = ListNutrientValStringConverter.fromListNutrientVal(nutrients)

        Assert.assertEquals(expected,actual)
    }

    @Test
    fun stringToListNutrientValueValid() {
        val expected = nutrients

        val actual = ListNutrientValStringConverter.fromString("0:1,1:2,2:3,322:455,46666:5,5:65,-1:0")

        Assert.assertEquals(expected,actual)
    }

    @Test
    fun stringToListNutrientValueEmpty() {
        val expected = ArrayList<NutrientValue>()

        val actual = ListNutrientValStringConverter.fromString("")

        Assert.assertEquals(expected,actual)
    }

    @Test
    fun goalStringValid() {
        val expected = "1,50"

        val actual = GoalStringConverter.fromGoal(Goal(1,50))

        Assert.assertEquals(expected,actual)
    }

    @Test
    fun stringGoalValid() {
        val expected = Goal(-2333,555555)

        val actual = GoalStringConverter.fromString("-2333,555555")

        Assert.assertEquals(expected,actual)
    }

    @Test
    fun portionStringValid() {
        val expected =
            "0${UNIT_DELIMITER}155.32${UNIT_DELIMITER}33$GROUP_DELIMITER" +
            "0${UNIT_DELIMITER}233.1${UNIT_DELIMITER}1$GROUP_DELIMITER" +
            "5${UNIT_DELIMITER}1.0${UNIT_DELIMITER}3$GROUP_DELIMITER" +
            "11111111${UNIT_DELIMITER}1554.55${UNIT_DELIMITER}9999$GROUP_DELIMITER" +
            "219${UNIT_DELIMITER}43.0${UNIT_DELIMITER}332222"

        val actual = ListPortionStringConverter.fromListPortion(portions)

        Assert.assertEquals(expected,actual)
    }

    @Test
    fun portionStringEmpty() {
        val expected = ""

        val actual = ListPortionStringConverter.fromListPortion(ArrayList())

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun stringPortionValid() {
        val expected = portions

        val actual = ListPortionStringConverter.fromString(
            "0${UNIT_DELIMITER}155.32${UNIT_DELIMITER}33$GROUP_DELIMITER" +
                "0${UNIT_DELIMITER}233.1${UNIT_DELIMITER}1$GROUP_DELIMITER" +
                "5${UNIT_DELIMITER}1.0${UNIT_DELIMITER}3$GROUP_DELIMITER" +
                "11111111${UNIT_DELIMITER}1554.55${UNIT_DELIMITER}9999$GROUP_DELIMITER" +
                "219${UNIT_DELIMITER}43.0${UNIT_DELIMITER}332222"
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun stringPortionEmpty() {
        val expected = ArrayList<Portion>()

        val actual = ListPortionStringConverter.fromString("")

        Assert.assertEquals(expected, actual)
    }
//    @Test
//    fun mapToString(){
//        val string = "alphabet${UNIT_DELIMITER}1${UNIT_DELIMITER}alphabets${UNIT_DELIMITER}2${UNIT_DELIMITER}alphabe${UNIT_DELIMITER}3"
//        val map = HashMap<String, Int>()
//        map.put("alphabet",1)
//        map.put("alphabets",2)
//        map.put("alphabe",3)
//        val res = HashMapStringConverter.fromHashMap(map)
//        Assert.assertEquals(string,res)
//    }
//
//    @Test
//    fun stringToMap(){
//        val string = "alphabet${UNIT_DELIMITER}1${UNIT_DELIMITER}alphabets${UNIT_DELIMITER}2${UNIT_DELIMITER}alphabe${UNIT_DELIMITER}3"
//        val map = HashMap<String, Int>()
//        map.put("alphabet",1)
//        map.put("alphabets",2)
//        map.put("alphabe",3)
//        val res = HashMapStringConverter.fromString(string)
//        Assert.assertEquals(map,res)
//    }
//
//    @Test
//    fun stringToMapToString(){
//        val string = "alphabet${UNIT_DELIMITER}1${UNIT_DELIMITER}alphabets${UNIT_DELIMITER}2${UNIT_DELIMITER}alphabe${UNIT_DELIMITER}3"
//        val res = HashMapStringConverter.fromHashMap(HashMapStringConverter.fromString(string))
//        Assert.assertEquals(string,res)
//    }

    //    @Test
//    fun listFoodToStringValid(){
//        val foods = this.foods
//        val expected =
//            "0${UNIT_DELIMITER}apple${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}0${GROUP_DELIMITER}" +
//            "555433${UNIT_DELIMITER}banana${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}55${GROUP_DELIMITER}" +
//            "3435${UNIT_DELIMITER}cactus${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}1${GROUP_DELIMITER}" +
//            "11111${UNIT_DELIMITER}deli meat!${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}1${GROUP_DELIMITER}" +
//            "55${UNIT_DELIMITER}eucalyptus${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}522"
//        val actual = ListFoodStringConverter.fromListFood(foods)
//
//        Assert.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun listFoodToStringEmpty() {
//        val foods = ArrayList<Food>()
//        val expected = ""
//
//        val actual = ListFoodStringConverter.fromListFood(foods)
//
//        Assert.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun stringToListFoodValid() {
//        val expected = foods
//
//        val actual = ListFoodStringConverter.fromString("0${UNIT_DELIMITER}apple${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}0${GROUP_DELIMITER}" +
//                "555433${UNIT_DELIMITER}banana${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}55${GROUP_DELIMITER}" +
//                "3435${UNIT_DELIMITER}cactus${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}1${GROUP_DELIMITER}" +
//                "11111${UNIT_DELIMITER}deli meat!${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}1${GROUP_DELIMITER}" +
//                "55${UNIT_DELIMITER}eucalyptus${UNIT_DELIMITER}0:1,1:2,2:3,322:455,46666:5,5:65,-1:0${UNIT_DELIMITER}522")
//
//        Assert.assertEquals(expected,actual)
//    }
//
//    @Test
//    fun stringToLIstFoodEmpty() {
//        val expected = ArrayList<Food>()
//
//        val actual = ListFoodStringConverter.fromString("")
//
//        Assert.assertEquals(expected,actual)
//    }
}