package com.happyhappyyay.badnutrition

import com.happyhappyyay.badnutrition.data.nutrient.calcNutrientValPercent
import org.junit.Test

import org.junit.Assert.*

class NutrientUnitTest {
    @Test
    fun normal_percentage_isCorrect() {
        val expected = 43
        val actual = calcNutrientValPercent(43,100)
        assertEquals(expected, actual)
    }

    @Test
    fun zero_goal_percentage_isCorrect() {
        val expected = 100
        val actual = calcNutrientValPercent(43,0)
        assertEquals(expected, actual)
    }

    @Test
    fun over_100_percentage_isCorrect() {
        val expected = 4300
        val actual = calcNutrientValPercent(43,1)
        assertEquals(expected, actual)
    }
}