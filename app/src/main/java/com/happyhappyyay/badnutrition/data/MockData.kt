package com.happyhappyyay.badnutrition.data

import com.happyhappyyay.badnutrition.data.nutrient.Goal
import com.happyhappyyay.badnutrition.data.nutrient.Nutrient
import kotlin.random.Random

class MockData {
    private val nutritionList = arrayOf("iron", "calcium", "manganese", "magnesium",
        "potassium", "sodium", "protein", "fat", "carbohydrates", "fiber",
        "sugar", "phosphorous", "iodine", "tocopherol", "calories")
    fun createNutritionList(): List<Nutrient> {
        val nutrients: ArrayList<Nutrient> = ArrayList()
        nutritionList.forEach { item ->
            nutrients.add(
                Nutrient(
                    name = item,
                    value = Random.nextInt(0,600),
                    measurement = "Kg",
                    goal = Goal(Random.nextInt(-50,500), Random.nextInt(-100,1000))
                )
            )
        }
        return nutrients
    }

    fun nutrientExample(): Nutrient {
        return Nutrient(
            name = "My Calories!",
            value = Random.nextInt(0,1000),
            measurement = "mg",
            goal = Goal(Random.nextInt(150,1000))
        )
    }
}