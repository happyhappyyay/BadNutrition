package com.happyhappyyay.badnutrition

import com.happyhappyyay.badnutrition.data.nutrient.calcNutrientValPercent
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
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