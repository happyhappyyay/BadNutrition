package com.happyhappyyay.badnutrition.data

import com.happyhappyyay.badnutrition.data.food.Food
import com.happyhappyyay.badnutrition.data.nutrient.Goal
import com.happyhappyyay.badnutrition.data.nutrient.Nutrient
import com.happyhappyyay.badnutrition.data.nutrient.NutrientValue
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
        foodList.forEachIndexed() { ind, item ->
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

    val nutrientChartPoints = arrayOf(50F,100F,0F,22F,33F,37F,30F,75F,72F,88F,120F,20F,39F,89F,100F,82F,78F,85F,82F,99F,92F,44F,22F,33F,37F,30F,75F,72F,88F,19F,20F)
    val nutrientChartPointsY = arrayOf(8F,16F,16F,82F,2F,94F,53F,85F,83F,5F,76F,29F,40F,25F,6F,39F,24F,38F,67F,7F,31F,1F,35F,2F,12F,27F,98F,8F,76F,77F,10F,45F,64F,91F,9F,29F,82F,82F,99F,91F,57F,63F,87F,33F,15F,1F,23F,58F,61F,47F,10F,17F,40F,64F,50F,52F,48F,24F,12F,90F,66F,4F,11F,20F,96F,86F,60F,74F,70F,36F,20F,31F,87F,84F,1F,57F,53F,49F,21F,30F,37F,32F,24F,88F,24F,28F,36F,90F,94F,67F,54F,9F,52F,68F,71F,17F,1F,80F,7F,52F,83F,72F,4F,1F,54F,64F,8F,15F,79F,76F,12F,79F,38F,63F,23F,55F,57F,62F,42F,64F,73F,91F,91F,40F,46F,64F,22F,72F,33F,88F,64F,22F,6F,6F,15F,46F,19F,74F,43F,15F,32F,24F,30F,3F,73F,5F,75F,97F,75F,59F,84F,19F,21F,31F,93F,87F,70F,31F,44F,73F,68F,66F,65F,49F,82F,45F,65F,79F,27F,97F,89F,11F,15F,36F,25F,25F,87F,36F,93F,18F,67F,91F,92F,24F,95F,93F,43F,4F,46F,60F,69F,13F,8F,48F,40F,77F,21F,24F,35F,37F,58F,23F,57F,43F,61F,35F,14F,22F,0F,52F,2F,63F,13F,49F,76F,18F,53F,97F,48F,35F,68F,71F,9F,51F,7F,2F,91F,53F,54F,41F,61F,39F,78F,65F,23F,5F,23F,89F,13F,52F,26F,12F,66F,26F,0F,88F,44F,55F,19F,78F,31F,32F,25F,87F,87F,70F,99F,96F,68F,50F,42F,93F,30F,64F,28F,31F,21F,49F,39F,98F,37F,67F,39F,87F,83F,35F,93F,73F,11F,59F,90F,18F,36F,76F,55F,5F,17F,94F,65F,51F,49F,60F,92F,12F,66F,37F,23F,71F,13F,59F,19F,10F,98F,89F,4F,35F,65F,10F,34F,75F,23F,3F,75F,43F,4F,33F,6F,32F,48F,52F,3F,63F,65F,76F,91F,94F,17F,13F,76F,87F,34F,86F,8F,71F,95F,53F,19F,45F,10F,14F,99F,98F,61F,32F,89F,15F,88F,66F,22F,73F,73F,22F,21F,0F,77F,77F,49F,24F,56F,46F,85F,36F,20F,87F,67F,46F)
    val nutrientChartPointsW = arrayOf(0F,100F,44F,22F,33F,37F,30F)
    val nutrientChartPointsWY = arrayOf(0F,100F,44F,22F,33F,37F,30F,75F,72F,88F,120F,20F,39F,89F,100F,82F,78F,85F,82F,99F,92F,44F,22F,33F,37F,30F,75F,72F,88F,19F,20F,0F,100F,44F,22F,33F,37F,30F,75F,72F,88F,120F,20F,39F,89F,100F,82F,78F,85F,82F,99F,55F)
    val nutrientItems = arrayOf(88F,38F,73F,92F,59F,37F,47F,11F,19F,33F,65F,31F,77F,21F,42F)
}