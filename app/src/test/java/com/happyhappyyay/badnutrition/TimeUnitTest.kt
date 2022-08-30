package com.happyhappyyay.badnutrition

import com.happyhappyyay.badnutrition.util.convertDayToMilli
import com.happyhappyyay.badnutrition.util.convertMilliToDay
import com.happyhappyyay.badnutrition.data.currentDateInMilliseconds
import com.happyhappyyay.badnutrition.util.incrementMilliDay
import com.happyhappyyay.badnutrition.data.nutrient.calcNutrientValPercent
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class TimeUnitTest {
    @Test
    fun normal_milli_day_isCorrect() {
        val actual = convertMilliToDay(1661749200000)
        val expected = "2022-08-29"
        assertEquals(expected, actual)
    }

    @Test
    fun normal_day_milli_isCorrect() {
        val expected = 1661749200000
        val actual = convertDayToMilli("2022-08-29")
        assertEquals(expected, actual)
    }

    @Test
    fun normal_add_time_isCorrect() {
        val expected = 1661835600000
        val actual = incrementMilliDay(convertDayToMilli("2022-08-29"),1)
        assertEquals(expected,actual)
    }

    @Test
    fun normal_remove_time_isCorrect() {
        val expected = 1661662800000
        val actual = incrementMilliDay(convertDayToMilli("2022-08-29"),-1)
        assertEquals(expected,actual)
    }

    @Test
    fun normal_add_zero_time_isCorrect(){
        val expected = 1661749200000
        val actual = incrementMilliDay(1661749200000,0)
        assertEquals(expected,actual)
    }
}