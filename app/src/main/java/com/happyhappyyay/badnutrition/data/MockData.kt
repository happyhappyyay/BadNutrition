package com.happyhappyyay.badnutrition.data

import androidx.compose.ui.graphics.Color
import com.happyhappyyay.badnutrition.ui.charts.GraphData
import com.happyhappyyay.badnutrition.data.food.Food
import com.happyhappyyay.badnutrition.data.nutrient.Goal
import com.happyhappyyay.badnutrition.data.nutrient.Nutrient
import com.happyhappyyay.badnutrition.data.nutrient.NutrientValue
import com.happyhappyyay.badnutrition.util.LegendItem
import kotlin.random.Random

class MockData {
    private val foodList = arrayOf("chicken", "orange", "tika masala", "potatoe", "short rib", "ice cream", "candy", "maple syrup", "onion", "potato chip")
    val nutritionList = arrayOf("iron", "calcium", "manganese", "magnesium",
        "potassium", "sodium", "protein", "fat", "carbohydrates", "fiber",
        "sugar", "phosphorous", "iodine", "tocopherol", "calories")
    fun createNutritionList(): ArrayList<Nutrient> {
        val nutrients: ArrayList<Nutrient> = ArrayList()
        nutritionList.forEach { item ->
            nutrients.add(
                Nutrient(
                    name = item,
                    value = Random.nextInt(0,100),
                    measurement = "Kg",
                    goal = Goal(Random.nextInt(-1,100), Random.nextInt(-1,100))
                )
            )
        }
        return nutrients
    }

    fun createFoodList(): ArrayList<Food> {
        val foods: ArrayList<Food> = ArrayList()
        foodList.forEachIndexed { ind, item ->
            foods.add(
                Food(ind*1L,item,ArrayList())
            )
        }
        return foods
    }

    fun nutrientExample(): Nutrient {
        return Nutrient(
            name = "My Calories!",
            value = Random.nextInt(0,1000),
            measurement = "mg",
            goal = Goal(Random.nextInt(150,1000))
        )
    }

    fun pointsFromNutrients(list:ArrayList<Nutrient>): Array<Float> {
        val arr = Array(list.size){ i ->
            val nutriPoint = list[i].value/(list[i].goal.min * 1F) * 100
            if(nutriPoint > 100) 110F else nutriPoint
            }
        return arr
    }

    fun nutrientsToNutrientValues(nutrients: List<Nutrient>):List<NutrientValue>{
        val values = arrayListOf<NutrientValue>()
        nutrients.forEachIndexed { i, nutrient ->
            values.add(NutrientValue(i,nutrient.value))
        }
        return values
    }

    val food = Food(1,"Spaghetti", nutrientsToNutrientValues(createNutritionList()))
}

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
