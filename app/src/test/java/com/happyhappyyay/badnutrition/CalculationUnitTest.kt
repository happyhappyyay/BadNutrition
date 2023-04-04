package com.happyhappyyay.badnutrition

import com.happyhappyyay.badnutrition.data.createTime
import com.happyhappyyay.badnutrition.data.nutrient.NutrientInfo
import com.happyhappyyay.badnutrition.ui.charts.GraphData
import com.happyhappyyay.badnutrition.ui.charts.GraphDataFilterOptions
import com.happyhappyyay.badnutrition.ui.charts.GraphDataTime
import com.happyhappyyay.badnutrition.ui.charts.GraphType
import com.happyhappyyay.badnutrition.ui.graph.*
import com.happyhappyyay.badnutrition.ui.util.GraphDataFilter
import org.junit.Assert
import org.junit.Test

class CalculationUnitTest {
    val dataFilterOptionsLine = GraphDataFilterOptions(type = GraphType.Line,GraphCategoryType.Food, GraphCalculationType.Mode,GraphMeasurementType.Amount, order = GraphOrderType.None)
    val dataFilterOptionsBar = GraphDataFilterOptions(type = GraphType.Bar,GraphCategoryType.Food, GraphCalculationType.Mode,GraphMeasurementType.Amount, order = GraphOrderType.None)
    val graphDataFilterL = GraphDataFilter(dataFilterOptionsLine, emptyList(), emptyMap(),
        listOf(createTime(0), createTime(1), createTime(2))
    )
    val graphDataFilterB = GraphDataFilter(dataFilterOptionsBar, emptyList(), emptyMap(),
        emptyList()
    )
    val graphData = arrayListOf(
        GraphData("calcium", 55F),
        GraphData("calcium", 11F),
        GraphData("calcium", 22F),
        GraphData("calcium", 5F),
        GraphData("calcium", 38F),
        GraphData("calcium", 15F),
        GraphData("calories", 1000F),
        GraphData("calories", 1255F),
        GraphData("calories", 99F),
        GraphData("calories", 0F),
        GraphData("protein", 2F),
        GraphData("protein", 15F),
        GraphData("protein", 1F),
        GraphData("protein", 7F),
        GraphData("protein", 7F),
    )

    @Test
    fun mean_amount_isCorrect() {
        val expected = arrayListOf(
            GraphData("calcium", 24.333334F),
            GraphData("calories", 588.5F),
            GraphData("protein", 6.4F)
        )
        val dataFilters = dataFilterOptionsBar
        val actual = graphDataFilterB.makeCalculation(GraphCalculationType.Mean, graphData)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun min_amount_isCorrect() {
        val expected = arrayListOf(
            GraphData("calcium", 5F),
            GraphData("calories", 0F),
            GraphData("protein", 1F)
        )
        val actual = graphDataFilterB.makeCalculation(GraphCalculationType.Min, graphData)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun max_amount_isCorrect() {
        val expected = arrayListOf(
            GraphData("calcium", 55F),
            GraphData("calories", 1255F),
            GraphData("protein", 15F)
        )
        val actual = graphDataFilterB.makeCalculation(GraphCalculationType.Max, graphData)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun sum_amount_isCorrect() {
        val expected = arrayListOf(
            GraphData("calcium", 146F),
            GraphData("calories", 2354F),
            GraphData("protein", 32F)
        )
        val actual = graphDataFilterB.makeCalculation(GraphCalculationType.Sum, graphData)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun mode_amount_isCorrect() {
        val expected = arrayListOf(
            GraphData("calcium", -24.333334F),
            GraphData("calories", -588.5F),
            GraphData("protein", 7F)
        )
        val actual = graphDataFilterB.makeCalculation(GraphCalculationType.Mode, graphData)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun mode_amount_time_all_isCorrect() {
        val list = listOf(
            GraphDataTime("a", 10F, createTime(0)),
            GraphDataTime("a",2F, createTime(0)),
            GraphDataTime("a",4F, createTime(0)),
            GraphDataTime("a",1F, createTime(1)),
            GraphDataTime("a",1F, createTime(1)),
            GraphDataTime("a",4F, createTime(1)),
            GraphDataTime("a",6F, createTime(2)),
            GraphDataTime("a",5F, createTime(2)),
        )

        val expected = listOf(GraphData("a",-16F/3), GraphData("a",1F), GraphData("a",-11F/2))
        val actual = graphDataFilterL.makeCalculationWithTime(GraphCalculationType.Mode, list)
        Assert.assertEquals(expected, actual)
    }
    @Test
    fun mode_amount_time_before_isCorrect() {
        val list = listOf(
            GraphDataTime("a",1F, createTime(1)),
            GraphDataTime("a",1F, createTime(1)),
            GraphDataTime("a",4F, createTime(1)),
            GraphDataTime("a",6F, createTime(2)),
            GraphDataTime("a",5F, createTime(2)),
        )

        val expected = listOf(GraphData("a",0F), GraphData("a",1F), GraphData("a",-11F/2))
        val actual = graphDataFilterL.makeCalculationWithTime(GraphCalculationType.Mode, list)
        Assert.assertEquals(expected, actual)
    }
    @Test
    fun mode_amount_time_after_isCorrect() {
        val list = listOf(
            GraphDataTime("a", 10F, createTime(0)),
            GraphDataTime("a",2F, createTime(0)),
            GraphDataTime("a",4F, createTime(0)),
            GraphDataTime("a",1F, createTime(1)),
            GraphDataTime("a",1F, createTime(1)),
            GraphDataTime("a",4F, createTime(1)),
        )

        val expected = listOf(GraphData("a",-16/3F), GraphData("a",1F), GraphData("a",0F))
        val actual = graphDataFilterL.makeCalculationWithTime(GraphCalculationType.Mode, list)
        Assert.assertEquals(expected, actual)
    }
    @Test
    fun mode_amount_time_between_isCorrect() {
        val list = listOf(
            GraphDataTime("a", 10F, createTime(0)),
            GraphDataTime("a",2F, createTime(0)),
            GraphDataTime("a",4F, createTime(0)),
            GraphDataTime("a",6F, createTime(2)),
            GraphDataTime("a",5F, createTime(2)),
        )

        val expected = listOf(GraphData("a",-16/3F), GraphData("a",0F), GraphData("a",-11/2F))
        val actual = graphDataFilterL.makeCalculationWithTime(GraphCalculationType.Mode, list)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun median_amount_isCorrect() {
        val expected = arrayListOf(
            GraphData("calcium", 18.5F),
            GraphData("calories", 549.5F),
            GraphData("protein", 7F)
        )
        val actual = graphDataFilterB.makeCalculation(GraphCalculationType.Median, graphData)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun max_amount_singular_all_isCorrect() {
        val list = listOf(
            GraphDataTime("a", 10F, createTime(0)),
            GraphDataTime("a",2F, createTime(0)),
            GraphDataTime("a",4F, createTime(0)),
            GraphDataTime("a",1F, createTime(1)),
            GraphDataTime("a",4F, createTime(1)),
            GraphDataTime("a",6F, createTime(2)),
            GraphDataTime("a",5F, createTime(2)),
            )
        val expected =listOf(GraphData("a",10F), GraphData("a",4F), GraphData("a",6F)
        )
        val actual = graphDataFilterL.makeCalculationWithTime(GraphCalculationType.Max,list)
        Assert.assertEquals(expected,actual)
    }

    @Test
    fun max_amount_singular_after_isCorrect() {
        val list = listOf(
            GraphDataTime("b",2F, createTime(0)),
            GraphDataTime("b",2F, createTime(0)),
            GraphDataTime("b",2F, createTime(1)),
        )
        val expected = listOf(GraphData("b",2F), GraphData("b",2F), GraphData("b",0F))
        val actual = graphDataFilterL.makeCalculationWithTime(GraphCalculationType.Max,list)
        Assert.assertEquals(expected,actual)
    }

    @Test
    fun max_amount_singular_before_isCorrect() {
        val list = listOf(
            GraphDataTime("c",2F, createTime(-1)),
            GraphDataTime("c",4F, createTime(2))
        )
        val expected = listOf(GraphData("c",2F), GraphData("c",0F), GraphData("c",4F))
        val actual = graphDataFilterL.makeCalculationWithTime(GraphCalculationType.Max,list)
        Assert.assertEquals(expected,actual)
    }

    @Test
    fun max_amount_singular_between_isCorrect() {
        val list = listOf(
            GraphDataTime("c",2F, createTime(0)),
            GraphDataTime("c",3F, createTime(0)),
            GraphDataTime("c",4F, createTime(0)),
            GraphDataTime("c",4F, createTime(3))
        )
        val expected = listOf(GraphData("c",4F), GraphData("c",0F), GraphData("c",4F))
        val actual = graphDataFilterL.makeCalculationWithTime(GraphCalculationType.Max,list)
        Assert.assertEquals(expected,actual)
    }
}