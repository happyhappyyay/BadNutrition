package com.happyhappyyay.badnutrition.data

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.happyhappyyay.badnutrition.data.food.Food
import com.happyhappyyay.badnutrition.data.food.Portion
import com.happyhappyyay.badnutrition.data.food.PortionOfFood
import com.happyhappyyay.badnutrition.data.nutrient.Goal
import com.happyhappyyay.badnutrition.data.nutrient.Nutrient
import com.happyhappyyay.badnutrition.data.nutrient.NutrientInfo
import com.happyhappyyay.badnutrition.data.nutrient.NutrientValue
import com.happyhappyyay.badnutrition.data.partition.Partition
import com.happyhappyyay.badnutrition.ui.charts.GraphData
import com.happyhappyyay.badnutrition.ui.util.LegendItem
import com.happyhappyyay.badnutrition.util.convertDayToMilli
import com.happyhappyyay.badnutrition.util.incrementMilliDay
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class MockData {
    private val foodList = arrayOf(
        "chicken",
        "orange",
        "tika masala",
        "potatoe",
        "short rib",
        "ice cream",
        "candy",
        "maple syrup",
        "onion",
        "potato chip"
    )
    val nutritionList = arrayOf(
        "iron", "calcium", "manganese", "magnesium",
        "potassium", "sodium", "protein", "fat", "carbohydrates", "fiber",
        "sugar", "phosphorous", "iodine", "tocopherol", "calories"
    )

    fun createNutritionList(): ArrayList<Nutrient> {
        val nutrients: ArrayList<Nutrient> = ArrayList()
        nutritionList.forEach { item ->
            nutrients.add(
                Nutrient(
                    name = item,
                    value = Random.nextInt(0, 100),
                    measurement = "Kg",
                    goal = Goal(Random.nextDouble(-1.0, 100.0), Random.nextDouble(-1.0, 100.0))
                )
            )
        }
        return nutrients
    }

    fun createFoodList(): ArrayList<Food> {
        val foods: ArrayList<Food> = ArrayList()
        foodList.forEachIndexed { ind, item ->
            foods.add(
                Food(ind * 1L, item, ArrayList())
            )
        }
        return foods
    }

    fun nutrientExample(): Nutrient {
        return Nutrient(
            name = "My Calories!",
            value = Random.nextInt(0, 1000),
            measurement = "mg",
            goal = Goal(Random.nextDouble(150.0, 1000.0))
        )
    }

    fun pointsFromNutrients(list: ArrayList<Nutrient>): Array<Float> {
        val arr = Array(list.size) { i ->
            val nutriPoint = list[i].value / (list[i].goal.min * 1F) * 100
            if (nutriPoint > 100) 110F else nutriPoint.toFloat()
        }
        return arr
    }

    fun nutrientsToNutrientValues(nutrients: List<Nutrient>): List<NutrientValue> {
        val values = arrayListOf<NutrientValue>()
        nutrients.forEachIndexed { i, nutrient ->
            values.add(NutrientValue(i * 1L, nutrient.value * 1.0))
        }
        return values
    }

    val food = Food(1, "Spaghetti", nutrientsToNutrientValues(createNutritionList()))
}

fun createRandomFoodList(): List<PortionOfFood> {
    val timeList = createTimeList()
    val list = mutableListOf<PortionOfFood>()
    val set = mutableSetOf<Long>()
//    250000 max
    for (i in 0..100034) {
        val time = timeList.random()
        set.add(time)
        val id = (foodIds.size * Math.random()).toInt()
        val partitionId = createRandomPartition()
        val amount = Math.random() * 100 + 1
        val portion = Portion(foodIds[id], foodIds[id], amount, partitionId, time)
        val food = Food(foodIds[id], foodNames[id], createRandomNutrientValues())
        val partition = Partition(partitionId, createPartitionString())
        list.add(PortionOfFood(portion, food, partition))
    }
    list.sortBy { it.portion.dateMs }
//    Log.d("MOCKDATA", "${set.size}")

    return list
}

val datePool =
    listOf("1999-01-01", "2002-02-14", "2011-03-22", "1992-04-11", "2032-05-01", "2100-06-19")

val partitionPool = listOf(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)

fun createRandomPartition(): Long {
    return partitionPool.random()
}

fun createRandomTime(): Long {
    return incrementMilliDay(convertDayToMilli(datePool[0]), (Math.random() * 365).toInt())
}
fun createTime(increment:Int): Long {
    return incrementMilliDay(convertDayToMilli(datePool[0]), increment)
}

val charPool = listOf('a', 'b', 'c')
val partitionNamePool =
    listOf("breakfast", "lunch", "dinner", "supper", "sandwich", "dessert", "snackies")

fun createPartitionString(): String {
    return partitionNamePool.random()
}

fun createRandomString(): String {
    return List(3) { charPool.random() }.joinToString("")
}

fun createRandomNutrientValues(): List<NutrientValue> {
    return nutrientMap.map { map ->
        NutrientValue(map.key, Math.random() * 100)
    }
}

fun createNutrientMap(): Map<Long, NutrientInfo> {
    val map = mutableMapOf<Long, NutrientInfo>()
    createRandomNutrientInfo().forEach {
        map[it.nutrientInfoId] = it
    }
    return map
}

fun createRandomNutrientInfo(): List<NutrientInfo> {
    var count = 0
    return nutrientMap.map { map ->
        NutrientInfo(
            map.key,
            map.value,
            "mg",
            count++,
            Goal(),
            Goal(Random.nextDouble(-1.0, 100.0), Random.nextDouble(-1.0, 100.0))
        )
    }
}

fun createTimeList(): List<Long> {
    val list = mutableListOf<Long>()
    val longDate = convertDayToMilli(datePool[0])
    var tempDate = longDate
    for (i in 0..364) {
        list.add(tempDate)
        tempDate = incrementMilliDay(tempDate)
    }
//    Log.d("MOCKDATA List", "${list.size}")
    return list
}

val sampleList = arrayListOf(
    GraphData("Calories", 25F),
    GraphData("Fat", 20F),
    GraphData("Carbohydrates", 35F),
    GraphData("Protein", 66F),
    GraphData("Sodium", 82F)
)

val nutrientMap = mapOf(
    Pair(1L, "Iron"),
    Pair(2L, "calcium"),
    Pair(3L, "manganese"),
    Pair(4L, "magnesium"),
    Pair(5L, "potassium"),
    Pair(6L, "sodium"),
    Pair(7L, "protein"),
    Pair(8L, "fat"),
    Pair(9L, "carbohydrates"),
    Pair(10L, "fiber"),
    Pair(11L, "sugar"),
    Pair(12L, "phosphorous"),
    Pair(13L, "iodine"),
    Pair(14L, "tocopherol"),
    Pair(15L, "calories")
)

val foodNames = arrayOf(
    "aaa",
    "aab",
    "aac",
    "aad",
    "aae",
    "aba",
    "abb",
    "abc",
    "abd",
    "abe",
    "aca",
    "acb",
    "acc",
    "acd",
    "ace",
    "ada",
    "adb",
    "adc",
    "add",
    "ade",
    "aea",
    "aeb",
    "aec",
    "aed",
    "aee",
    "baa",
    "bab",
    "bac",
    "bad",
    "bae",
    "bba",
    "bbb",
    "bbc",
    "bbd",
    "bbe",
    "bca",
    "bcb",
    "bcc",
    "bcd",
    "bce",
    "bda",
    "bdb",
    "bdc",
    "bdd",
    "bde",
    "bea",
    "beb",
    "bec",
    "bed",
    "bee",
    "caa",
    "cab",
    "cac",
    "cad",
    "cae",
    "cba",
    "cbb",
    "cbc",
    "cbd",
    "cbe",
    "cca",
    "ccb",
    "ccc",
    "ccd",
    "cce",
    "cda",
    "cdb",
    "cdc",
    "cdd",
    "cde",
    "cea",
    "ceb",
    "cec",
    "ced",
    "cee",
    "daa",
    "dab",
    "dac",
    "dad",
    "dae",
    "dba",
    "dbb",
    "dbc",
    "dbd",
    "dbe",
    "dca",
    "dcb",
    "dcc",
    "dcd",
    "dce",
    "dda",
    "ddb",
    "ddc",
    "ddd",
    "dde",
    "dea",
    "deb",
    "dec",
    "ded",
    "dee",
    "eaa",
    "eab",
    "eac",
    "ead",
    "eae",
    "eba",
    "ebb",
    "ebc",
    "ebd",
    "ebe",
    "eca",
    "ecb",
    "ecc",
    "ecd",
    "ece",
    "eda",
    "edb",
    "edc",
    "edd",
    "ede",
    "eea",
    "eeb",
    "eec",
    "eed",
    "eee"
)

val foodIds = arrayOf(
    0L,
    1L,
    2L,
    3L,
    4L,
    10L,
    11L,
    12L,
    13L,
    14L,
    20L,
    21L,
    22L,
    23L,
    24L,
    30L,
    31L,
    32L,
    33L,
    34L,
    40L,
    41L,
    42L,
    43L,
    44L,
    100L,
    101L,
    102L,
    103L,
    104L,
    110L,
    111L,
    112L,
    113L,
    114L,
    120L,
    121L,
    122L,
    123L,
    124L,
    130L,
    131L,
    132L,
    133L,
    134L,
    140L,
    141L,
    142L,
    143L,
    144L,
    200L,
    201L,
    202L,
    203L,
    204L,
    210L,
    211L,
    212L,
    213L,
    214L,
    220L,
    221L,
    222L,
    223L,
    224L,
    230L,
    231L,
    232L,
    233L,
    234L,
    240L,
    241L,
    242L,
    243L,
    244L,
    300L,
    301L,
    302L,
    303L,
    304L,
    310L,
    311L,
    312L,
    313L,
    314L,
    320L,
    321L,
    322L,
    323L,
    324L,
    330L,
    331L,
    332L,
    333L,
    334L,
    340L,
    341L,
    342L,
    343L,
    344L,
    400L,
    401L,
    402L,
    403L,
    404L,
    410L,
    411L,
    412L,
    413L,
    414L,
    420L,
    421L,
    422L,
    423L,
    424L,
    430L,
    431L,
    432L,
    433L,
    434L,
    440L,
    441L,
    442L,
    443L,
    444L
)

val gList = arrayListOf(
    GraphData("pAAty", 100F), GraphData("Rooice", 20F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        60F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("apples", 70F),
    GraphData("snaekies", 60F),
    GraphData("Sugar", 20F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 22F),
    GraphData("Protein", 44F),
    GraphData("Carbohydrates", 1F),
    GraphData("Sodium", 68F),
    GraphData("Sugar", 75F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        49F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 23F),
    GraphData("Ash", 24F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 23F),
    GraphData("Calcium", 24F),
    GraphData("Manganese", 25F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil", 24F
    ),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Carbohydrates", 23F),
    GraphData("Sodium", 22F),
    GraphData("Sugar", 21F),
    GraphData("Saturated Fat", 20F),
    GraphData("Unsaturated Fat", 21F),
    GraphData("Calories", 22F),
    GraphData("Tocopherol", 23F),
    GraphData("Ash", 24F),
    GraphData("Calcium", 25F),
    GraphData("Manganese", 26F),
    GraphData("Protein", 27F),
    GraphData("Carbohydrates", 28F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Sodium", 29F),
    GraphData("Sugar", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 30F),
    GraphData("Ash", 29F),
    GraphData("Ash", 28F),
    GraphData("Ash", 27F),
    GraphData("Ash", 26F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 19F),
    GraphData("Ash", 18F),
    GraphData("Ash", 17F),
    GraphData("Ash", 16F),
    GraphData("Ash", 15F),
    GraphData("Ash", 14F),
    GraphData("Ash", 13F),
    GraphData("Ash", 12F),
    GraphData("Ash", 11F),
    GraphData("Ash", 10F),
    GraphData("Ash", 9F),
    GraphData("Ash", 8F),
    GraphData("Ash", 7F),
    GraphData("Ash", 6F),
    GraphData("Ash", 6F),
    GraphData("Ash", 5F),
    GraphData("Ash", 4F),
    GraphData("Ash", 3F),
    GraphData("Ash", 2F),
    GraphData("Ash", 1F),
    GraphData("Ash", 0F),
    GraphData("MEAT", 100F),
    GraphData("MEAT", 100F),
    GraphData("MEAT", 100F)
)
val hList = arrayListOf(
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        49F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        49F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("pAAty", 100F), GraphData("Rooice", 20F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        60F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("apples", 70F),
    GraphData("snaekies", 60F),
    GraphData("Sugar", 20F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 22F),
    GraphData("Protein", 44F),
    GraphData("Carbohydrates", 1F),
    GraphData("Sodium", 68F),
    GraphData("Sugar", 75F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        49F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 23F),
    GraphData("Ash", 24F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 23F),
    GraphData("Calcium", 24F),
    GraphData("Manganese", 25F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil", 24F
    ),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Carbohydrates", 23F),
    GraphData("Sodium", 22F),
    GraphData("Sugar", 21F),
    GraphData("Saturated Fat", 20F),
    GraphData("Unsaturated Fat", 21F),
    GraphData("Calories", 22F),
    GraphData("Tocopherol", 23F),
    GraphData("Ash", 24F),
    GraphData("Calcium", 25F),
    GraphData("Manganese", 26F),
    GraphData("Protein", 27F),
    GraphData("Carbohydrates", 28F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Sodium", 29F),
    GraphData("Sugar", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 30F),
    GraphData("Ash", 29F),
    GraphData("Ash", 28F),
    GraphData("Ash", 27F),
    GraphData("Ash", 26F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 19F),
    GraphData("Ash", 18F),
    GraphData("Ash", 17F),
    GraphData("Ash", 16F),
    GraphData("Ash", 15F),
    GraphData("Ash", 14F),
    GraphData("Ash", 13F),
    GraphData("Ash", 12F),
    GraphData("Ash", 11F),
    GraphData("Ash", 10F),
    GraphData("Ash", 9F),
    GraphData("Ash", 8F),
    GraphData("Ash", 7F),
    GraphData("Ash", 6F),
    GraphData("Ash", 6F),
    GraphData("Ash", 5F),
    GraphData("Ash", 4F),
    GraphData("Ash", 3F),
    GraphData("Ash", 2F),
    GraphData("Ash", 1F),
    GraphData("Ash", 0F),
    GraphData("pAAty", 100F), GraphData("Rooice", 20F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        60F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("apples", 70F),
    GraphData("snaekies", 60F),
    GraphData("Sugar", 20F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 22F),
    GraphData("Protein", 44F),
    GraphData("Carbohydrates", 1F),
    GraphData("Sodium", 68F),
    GraphData("Sugar", 75F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        49F
    ),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 23F),
    GraphData("Ash", 24F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 21F),
    GraphData("Ash", 22F),
    GraphData("Ash", 23F),
    GraphData("Calcium", 24F),
    GraphData("Manganese", 25F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil", 24F
    ),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Carbohydrates", 23F),
    GraphData("Sodium", 22F),
    GraphData("Sugar", 21F),
    GraphData("Saturated Fat", 20F),
    GraphData("Unsaturated Fat", 21F),
    GraphData("Calories", 22F),
    GraphData("Tocopherol", 23F),
    GraphData("Ash", 24F),
    GraphData("Calcium", 25F),
    GraphData("Manganese", 26F),
    GraphData("Protein", 27F),
    GraphData("Carbohydrates", 28F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Sodium", 29F),
    GraphData("Sugar", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 12F),
    GraphData("Unsaturated Fat", 88F),
    GraphData("Calories", 94F),
    GraphData("Tocopherol", 73F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Calcium", 100F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Ash", 48F),
    GraphData("Calcium", 9F),
    GraphData("Manganese", 90F),
    GraphData("Protein", 80F),
    GraphData("Carbohydrates", 70F),
    GraphData("Sodium", 60F),
    GraphData("Sugar", 50F),
    GraphData(
        "Chicken (raw) 500 mg minimum per grease ball cooked and unsalted in extra oil, extra cripsy on the inside and the outside of the whole chicken breast patty of meat",
        80F
    ),
    GraphData("Saturated Fat", 40F),
    GraphData("Unsaturated Fat", 30F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Calories", 20F),
    GraphData("Tocopherol", 10F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 49F),
    GraphData("Ash", 30F),
    GraphData("Ash", 29F),
    GraphData("Ash", 28F),
    GraphData("Ash", 27F),
    GraphData("Ash", 26F),
    GraphData("Ash", 25F),
    GraphData("Ash", 24F),
    GraphData("Ash", 23F),
    GraphData("Ash", 22F),
    GraphData("Ash", 21F),
    GraphData("Ash", 20F),
    GraphData("Ash", 19F),
    GraphData("Ash", 18F),
    GraphData("Ash", 17F),
    GraphData("Ash", 16F),
    GraphData("Ash", 15F),
    GraphData("Ash", 14F),
    GraphData("Ash", 13F),
    GraphData("Ash", 12F),
    GraphData("Ash", 11F),
    GraphData("Ash", 10F),
    GraphData("Ash", 9F),
    GraphData("Ash", 8F),
    GraphData("Ash", 7F),
    GraphData("Ash", 6F),
    GraphData("Ash", 6F),
    GraphData("Ash", 5F),
    GraphData("Ash", 4F),
    GraphData("Ash", 3F),
    GraphData("Ash", 2F),
    GraphData("Ash", 1F),
    GraphData("Ash", 0F),
)
val values = arrayOf(
    "100", "80", "60", "40", "20", "0%"
)

val condensedValues = arrayOf(
    "100", "50", "0%"
)
val legendaryItems = arrayOf(
    LegendItem(
        "Milk, reduced fat, fluid 2%,milkfat, with added nonfat milk solids and vitamin a and vitamin d ",
        Color.Red
    ),
    LegendItem(
        "Carbohydrates", Color.Magenta
    ),
    LegendItem("Calories", Color.Black),
    LegendItem("Fat", Color.Blue),
    LegendItem("Onion Rings", Color.Green)
)
