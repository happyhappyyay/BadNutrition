package com.happyhappyyay.badnutrition.ui.util

import com.happyhappyyay.badnutrition.data.food.PortionOfFood
import com.happyhappyyay.badnutrition.data.nutrient.NutrientInfo
import com.happyhappyyay.badnutrition.data.nutrient.calcNutrientValPercent
import com.happyhappyyay.badnutrition.ui.charts.GraphData
import com.happyhappyyay.badnutrition.ui.charts.GraphDataFilterOptions
import com.happyhappyyay.badnutrition.ui.charts.GraphDataTime
import com.happyhappyyay.badnutrition.ui.charts.GraphType
import com.happyhappyyay.badnutrition.ui.graph.GraphCalculationType
import com.happyhappyyay.badnutrition.ui.graph.GraphCategoryType
import com.happyhappyyay.badnutrition.ui.graph.GraphMeasurementType
import com.happyhappyyay.badnutrition.ui.graph.GraphOrderType
import com.happyhappyyay.badnutrition.util.MedianFinder
import com.happyhappyyay.badnutrition.util.convertMilliToDay

class GraphDataFilter(
    private val graphDataFilterOptions: GraphDataFilterOptions,
    private val updatedFoodPortions: List<PortionOfFood>,
    private val nutrients: Map<Long, NutrientInfo>,
    private val times: List<Long>
) {

    fun createGraphList(): List<List<GraphData>> {
        return when (graphDataFilterOptions.type) {
            GraphType.Bar -> {
                filterCumulativeGraph(
                    graphDataFilterOptions.category,
                    graphDataFilterOptions.calculation,
                    graphDataFilterOptions.measure,
                    graphDataFilterOptions.order
                )
            }
            else -> {
                filterSeparateGraph(
                    graphDataFilterOptions.category,
                    graphDataFilterOptions.calculation,
                    graphDataFilterOptions.measure,
                    graphDataFilterOptions.order
                )
            }
        }
    }

    private fun filterCumulativeGraph(
        category: GraphCategoryType,
        calculation: GraphCalculationType,
        measurement: GraphMeasurementType,
        order: GraphOrderType
    ): List<List<GraphData>> {
        return listOf(
            when (category) {
                GraphCategoryType.Food -> {
                    filterFoodCategoryCumulative(calculation, measurement, order)
                }
                GraphCategoryType.Nutrient -> {
                    filterNutrientCategoryCumulative(calculation, measurement, order)
                }
                GraphCategoryType.Time -> {
                    filterTimeCategoryCumulative(calculation, measurement, order)
                }
                GraphCategoryType.Partition -> {
                    filterPartitionCategoryCumulative(calculation, measurement, order)
                }
            }
        )
    }

    //    Assumes time sorted items
    private fun filterSeparateGraph(
        category: GraphCategoryType,
        calculation: GraphCalculationType,
        measurement: GraphMeasurementType,
        order: GraphOrderType
    ): List<List<GraphData>> {
        return when (category) {
            GraphCategoryType.Food -> {
                filterFoodCategorySingular(calculation, measurement, order)
            }
            GraphCategoryType.Nutrient -> {
                filterNutrientCategorySingular(calculation, measurement, order)
            }
            else -> {
                filterPartitionCategorySingular(calculation, measurement, order)
            }
        }
    }

    private fun filterFoodCategoryCumulative(
        calculation: GraphCalculationType, measurement: GraphMeasurementType, order: GraphOrderType
    ): List<GraphData> {
        val rawGraphData = mutableListOf<GraphData>()
        val len = updatedFoodPortions.size
        updatedFoodPortions.forEach { portion ->
            when (measurement) {
                GraphMeasurementType.Amount -> {
                    rawGraphData.add(GraphData(portion.food.name, portion.portion.amount.toFloat()))
                }
                GraphMeasurementType.Entries -> {
                    rawGraphData.add(GraphData(portion.food.name, 1F))
                }
                GraphMeasurementType.Percent -> {
                    rawGraphData.add(GraphData(portion.food.name, (1F / len) * 100))
                }
                GraphMeasurementType.Serving -> {
                    rawGraphData.add(GraphData(portion.food.name, portion.portion.amount.toFloat()))
                }
            }
        }
        return makeCalculation(calculation, rawGraphData)
    }

    fun filterFoodCategorySingular(
        calculation: GraphCalculationType, measurement: GraphMeasurementType, order: GraphOrderType
    ): List<List<GraphData>> {
        val rawGraphData = mutableListOf<MutableList<GraphDataTime>>()
        val nameIndexPair = mutableMapOf<Long, Int>()
        val set = mutableSetOf<Long>()
//      sorted by date, keep sorted by following list of times
        var count = -1
        when (measurement) {
            GraphMeasurementType.Amount -> {
                updatedFoodPortions.forEach { portion ->
                    val listIndex = nameIndexPair[portion.food.id]
                    val graphData = GraphDataTime(
                        portion.food.name, portion.portion.amount.toFloat(), portion.portion.dateMs
                    )
                    set.add(portion.portion.dateMs)
                    if (listIndex == null) {
                        nameIndexPair[portion.food.id] = ++count
                        rawGraphData.add(mutableListOf(graphData))
                    } else {
                        rawGraphData[listIndex].add(graphData)
                    }
                }
            }
            GraphMeasurementType.Entries -> {
                updatedFoodPortions.forEach { portion ->
                    val listIndex = nameIndexPair[portion.food.id]
                    val graphData = GraphDataTime(portion.food.name, 1F, portion.portion.dateMs)
                    if (listIndex == null) {
                        nameIndexPair[portion.food.id] = ++count
                        rawGraphData.add(mutableListOf(graphData))
                    } else {
                        rawGraphData[listIndex].add(graphData)
                    }
                }
            }
            GraphMeasurementType.Percent -> {
                updatedFoodPortions.forEach { portion ->
                    val listIndex = nameIndexPair[portion.food.id]
                    val graphData = GraphDataTime(
                        portion.food.name, portion.portion.amount.toFloat(), portion.portion.dateMs
                    )
                    if (listIndex == null) {
                        nameIndexPair[portion.food.id] = ++count
                        rawGraphData.add(mutableListOf(graphData))
                    } else {
                        rawGraphData[listIndex].add(graphData)
                    }
                }
            }
            GraphMeasurementType.Serving -> {
                updatedFoodPortions.forEach { portion ->
                    val listIndex = nameIndexPair[portion.food.id]
                    val graphData = GraphDataTime(
                        portion.food.name, portion.portion.amount.toFloat(), portion.portion.dateMs
                    )
                    if (listIndex == null) {
                        nameIndexPair[portion.food.id] = ++count
                        rawGraphData.add(mutableListOf(graphData))
                    } else {
                        rawGraphData[listIndex].add(graphData)
                    }
                }
            }
        }
        return rawGraphData.map { data ->
            makeCalculationWithTime(calculation, data)
        }
    }

    private fun filterNutrientCategorySingular(
        calculation: GraphCalculationType, measurement: GraphMeasurementType, order: GraphOrderType
    ): List<List<GraphData>> {
        val nutrientsSize = nutrients.size
        val rawGraphData =
            MutableList<MutableList<GraphDataTime>>(nutrientsSize) { mutableListOf() }
        val len = updatedFoodPortions.size
        when (measurement) {
            GraphMeasurementType.Amount -> {
                updatedFoodPortions.forEach { portion ->
                    portion.food.nutrients.forEach { nutrient ->
                        val nutrientInfo = nutrients[nutrient.nameId]
                        if (nutrientInfo != null) {
                            val graphData = GraphDataTime(
                                nutrientInfo.name, nutrient.value.toFloat(), portion.portion.dateMs
                            )
                            rawGraphData[nutrientInfo.order].add(graphData)
                        }
                    }
                }
            }
            GraphMeasurementType.Entries -> {
                updatedFoodPortions.forEach { portion ->
                    portion.food.nutrients.forEach { nutrient ->
                        val nutrientInfo = nutrients[nutrient.nameId]
                        if (nutrientInfo != null) {
                            val graphData =
                                GraphDataTime(nutrientInfo.name, 1F, portion.portion.dateMs)
                            rawGraphData[nutrientInfo.order].add(graphData)
                        }
                    }
                }
            }
            GraphMeasurementType.Percent -> {
                updatedFoodPortions.forEach { portion ->
                    portion.food.nutrients.forEach { nutrient ->
                        val nutrientInfo = nutrients[nutrient.nameId]
                        if (nutrientInfo != null) {
                            val nutrientName = nutrientInfo.name
                            val relVal =
                                calcNutrientValPercent(nutrient.value, nutrientInfo.goal.min)
                            rawGraphData[nutrientInfo.order].add(
                                GraphDataTime(
                                    nutrientName, relVal, portion.portion.dateMs
                                )
                            )
                        }
                    }
                }
            }
            GraphMeasurementType.Serving -> {
                updatedFoodPortions.forEach { portion ->
                    portion.food.nutrients.forEach { nutrient ->
                        val nutrientInfo = nutrients[nutrient.nameId]
                        if (nutrientInfo != null) {
                            val graphData = GraphDataTime(
                                nutrientInfo.name, nutrient.value.toFloat(), portion.portion.dateMs
                            )
                            rawGraphData[nutrientInfo.order].add(graphData)
                        }
                    }
                }
            }
        }
        val filteredNutrients = rawGraphData.filter { data ->
            data.isNotEmpty()
        }
        return filteredNutrients.map { data ->
            makeCalculationWithTime(calculation, data)
        }
    }

    private fun filterNutrientCategoryCumulative(
        calculation: GraphCalculationType, measurement: GraphMeasurementType, order: GraphOrderType
    ): List<GraphData> {
        val rawGraphData = mutableListOf<GraphData>()
        when (measurement) {
            GraphMeasurementType.Amount -> {
                updatedFoodPortions.forEach { portion ->
                    portion.food.nutrients.forEach { nutrient ->
                        val nutrientInfo = nutrients[nutrient.nameId]
                        if (nutrientInfo != null) {
                            val graphData = GraphData(nutrientInfo.name, nutrient.value.toFloat())
                            rawGraphData.add(graphData)
                        }
                    }

                }
            }
            GraphMeasurementType.Entries -> {
                updatedFoodPortions.forEach { portion ->
                    portion.food.nutrients.forEach { nutrient ->
                        val nutrientInfo = nutrients[nutrient.nameId]
                        if (nutrientInfo != null) {
                            val nutrientValue = nutrient.value
                            val graphData =
                                GraphData(nutrientInfo.name, if (nutrientValue > 0) 1F else 0F)
                            rawGraphData.add(graphData)
                        }
                    }

                }
            }
            GraphMeasurementType.Percent -> {
                updatedFoodPortions.forEach { portion ->
                    portion.food.nutrients.forEach { nutrient ->
                        val nutrientInfo = nutrients[nutrient.nameId]
                        if (nutrientInfo != null) {
                            val nutrientName = nutrientInfo.name
                            val relVal =
                                calcNutrientValPercent(nutrient.value, nutrientInfo.goal.min)
                            rawGraphData.add(GraphData(nutrientName, relVal))
                        }
                    }

                }
            }
            GraphMeasurementType.Serving -> {
                updatedFoodPortions.forEach { portion ->
                    portion.food.nutrients.forEach { nutrient ->
                        val nutrientInfo = nutrients[nutrient.nameId]
                        if (nutrientInfo != null) {
                            val nutrientValue = nutrient.value
                            val graphData =
                                GraphData(nutrientInfo.name, if (nutrientValue > 0) 1F else 0F)
                            rawGraphData.add(graphData)
                        }
                    }
                }
            }
        }
        return makeCalculation(calculation, rawGraphData)
    }

    private fun filterTimeCategoryCumulative(
        calculation: GraphCalculationType, measurement: GraphMeasurementType, order: GraphOrderType
    ): List<GraphData> {
        val rawGraphData = mutableListOf<GraphData>()
        val len = updatedFoodPortions.size
        updatedFoodPortions.forEach { portion ->
            val dateISO = convertMilliToDay(portion.portion.dateMs)
            when (measurement) {
                GraphMeasurementType.Amount -> {
                    rawGraphData.add(GraphData(dateISO, 1F))
                }
                GraphMeasurementType.Entries -> {
                    rawGraphData.add(GraphData(dateISO, 1F))
                }
                GraphMeasurementType.Percent -> {
                    rawGraphData.add(GraphData(dateISO, (1F / len) * 100))
                }
                GraphMeasurementType.Serving -> {
                    rawGraphData.add(GraphData(dateISO, portion.portion.amount.toFloat()))
                }
            }
        }
        return makeCalculation(calculation, rawGraphData)
    }

    private fun filterPartitionCategorySingular(
        calculation: GraphCalculationType, measurement: GraphMeasurementType, order: GraphOrderType
    ): List<List<GraphData>> {
        val rawGraphData = mutableListOf<MutableList<GraphDataTime>>()
        val nameIndexPair = mutableMapOf<Long, Int>()
        val len = updatedFoodPortions.size
        var nameIndexCount = -1
        when (measurement) {
            GraphMeasurementType.Amount -> {
                updatedFoodPortions.forEach { portion ->
                    val listIndex = nameIndexPair[portion.partition.id]
                    val graphData =
                        GraphDataTime(portion.partition.name, 1F, portion.portion.dateMs)
                    if (listIndex == null) {
                        nameIndexPair[portion.partition.id] = ++nameIndexCount
                        rawGraphData.add(mutableListOf(graphData))
                    } else {
                        rawGraphData[listIndex].add(graphData)
                    }
                }
            }
            GraphMeasurementType.Entries -> {
                updatedFoodPortions.forEach { portion ->
                    val listIndex = nameIndexPair[portion.partition.id]
                    val graphData =
                        GraphDataTime(portion.partition.name, 1F, portion.portion.dateMs)
                    if (listIndex == null) {
                        nameIndexPair[portion.partition.id] = ++nameIndexCount
                        rawGraphData.add(mutableListOf(graphData))
                    } else {
                        rawGraphData[listIndex].add(graphData)
                    }
                }
            }
            GraphMeasurementType.Percent -> {
                updatedFoodPortions.forEach { portion ->
                    val listIndex = nameIndexPair[portion.food.id]
                    val graphData = GraphDataTime(
                        portion.food.name, portion.portion.amount.toFloat(), portion.portion.dateMs
                    )
                    if (listIndex == null) {
                        nameIndexPair[portion.partition.id] = ++nameIndexCount
                        rawGraphData.add(mutableListOf(graphData))
                    } else {
                        rawGraphData[listIndex].add(graphData)
                    }
                }
            }
            GraphMeasurementType.Serving -> {
                updatedFoodPortions.forEach { portion ->
                    val listIndex = nameIndexPair[portion.partition.id]
                    val graphData = GraphDataTime(
                        portion.partition.name, (1F / len) * 100, portion.portion.dateMs
                    )
                    if (listIndex == null) {
                        nameIndexPair[portion.partition.id] = ++nameIndexCount
                        rawGraphData.add(mutableListOf(graphData))
                    } else {
                        rawGraphData[listIndex].add(graphData)
                    }
                }
            }
        }
        return rawGraphData.map { data ->
            makeCalculationWithTime(calculation, data)
        }
    }

    private fun filterPartitionCategoryCumulative(
        calculation: GraphCalculationType, measurement: GraphMeasurementType, order: GraphOrderType
    ): List<GraphData> {
        val rawGraphData = mutableListOf<GraphData>()
        val len = updatedFoodPortions.size
        updatedFoodPortions.forEach { portion ->
            val partitionName = portion.partition.name
            when (measurement) {
                GraphMeasurementType.Amount -> {
                    rawGraphData.add(GraphData(partitionName, 1F))
                }
                GraphMeasurementType.Entries -> {
                    rawGraphData.add(GraphData(partitionName, 1F))
                }
                GraphMeasurementType.Percent -> {
                    rawGraphData.add(GraphData(partitionName, (1F / len) * 100))
                }
                GraphMeasurementType.Serving -> {
                    rawGraphData.add(GraphData(partitionName, portion.portion.amount.toFloat()))
                }
            }
        }
        return makeCalculation(calculation, rawGraphData)
    }

    fun makeCalculationWithTime(
        calculation: GraphCalculationType,
        rawGraphData: List<GraphDataTime>,
    ): List<GraphData> {
        val name = rawGraphData[0].name
        val timeIndexMap = mutableMapOf<Long, Int>()
        var timePosition = 0
        var timesIndex = 0
        var graphDataCount = 0
        val earliestDate = rawGraphData[0].time
//        conditionally branch based on calculation because some calculations require all of the data
        return if (calculation == GraphCalculationType.Sum || calculation == GraphCalculationType.Min || calculation == GraphCalculationType.Max) {
            val nameValueCalculation = mutableMapOf<Long, Float>()
            when (calculation) {
                GraphCalculationType.Max -> {
//                  add missing times to a map (before)
                    while (timesIndex < times.size && times[timesIndex] < earliestDate) {
                        timeIndexMap[times[timesIndex]] = timePosition++
                        nameValueCalculation[times[timesIndex++]] = 0F
                    }
//                  add the times from the first through the last data entry
                    while (timesIndex < times.size && graphDataCount < rawGraphData.size) {
                        if (rawGraphData[graphDataCount].time <= times[timesIndex]) {
                            while (graphDataCount < rawGraphData.size && rawGraphData[graphDataCount].time <= times[timesIndex]) {
                                val graphDatum = rawGraphData[graphDataCount++]
                                val prevVal = nameValueCalculation[times[timesIndex]]
                                if (prevVal == null || prevVal < graphDatum.value) {
                                    nameValueCalculation[times[timesIndex]] = graphDatum.value
                                }
                            }
                            timeIndexMap[times[timesIndex++]] = timePosition++
                        } else {
                            val graphDatum = rawGraphData[graphDataCount]
                            while (timesIndex < times.size && times[timesIndex] < graphDatum.time) {
                                timeIndexMap[times[timesIndex]] = timePosition++
                                nameValueCalculation[times[timesIndex++]] = 0F
                            }
                        }
                    }
//                  add missing times to a map (after)
                    while (timesIndex < times.size) {
                        timeIndexMap[times[timesIndex]] = timePosition++
                        nameValueCalculation[times[timesIndex++]] = 0F
                    }
                    if(times.isNotEmpty()) {
                        while (graphDataCount < rawGraphData.size) {
                            val graphDatum = rawGraphData[graphDataCount++]
                            val prevVal = nameValueCalculation[times[timesIndex - 1]]
                            if (prevVal == null || prevVal < graphDatum.value) {
                                nameValueCalculation[times[timesIndex - 1]] = graphDatum.value
                            }
                        }
                    }
                }
                GraphCalculationType.Min -> {
                    while (timesIndex < times.size && times[timesIndex] < earliestDate) {
                        timeIndexMap[times[timesIndex]] = timePosition++
                        nameValueCalculation[times[timesIndex++]] = 0F
                    }
//                  add the times from the first through the last data entry
                    while (timesIndex < times.size && graphDataCount < rawGraphData.size) {
                        if (rawGraphData[graphDataCount].time <= times[timesIndex]) {
                            while (graphDataCount < rawGraphData.size && rawGraphData[graphDataCount].time <= times[timesIndex]) {
                                val graphDatum = rawGraphData[graphDataCount++]
                                val prevVal = nameValueCalculation[times[timesIndex]]
                                if (prevVal == null || prevVal > graphDatum.value) {
                                    nameValueCalculation[times[timesIndex]] = graphDatum.value
                                }
                            }
                            timeIndexMap[times[timesIndex++]] = timePosition++
                        } else {
                            val graphDatum = rawGraphData[graphDataCount]
                            while (timesIndex < times.size && times[timesIndex] < graphDatum.time) {
                                timeIndexMap[times[timesIndex]] = timePosition++
                                nameValueCalculation[times[timesIndex++]] = 0F
                            }
                        }
                    }
//                  add missing times to a map (after)
                    while (timesIndex < times.size) {
                        timeIndexMap[times[timesIndex]] = timePosition++
                        nameValueCalculation[times[timesIndex++]] = 0F
                    }
                    if(times.isNotEmpty()) {
                        while(graphDataCount < rawGraphData.size) {
                            val graphDatum = rawGraphData[graphDataCount++]
                            val prevVal = nameValueCalculation[times[timesIndex - 1]]
                            if (prevVal == null || prevVal > graphDatum.value) {
                                nameValueCalculation[times[timesIndex - 1]] = graphDatum.value
                            }
                        }
                    }
                }
//                    SUM
                else -> {
                    while (timesIndex < times.size && times[timesIndex] < earliestDate) {
                        timeIndexMap[times[timesIndex]] = timePosition++
                        nameValueCalculation[times[timesIndex++]] = 0F
                    }
//                  add the times from the first through the last data entry
                    while (timesIndex < times.size && graphDataCount < rawGraphData.size) {
                        if (rawGraphData[graphDataCount].time <= times[timesIndex]) {
                            while (graphDataCount < rawGraphData.size && rawGraphData[graphDataCount].time <= times[timesIndex]) {
                                val graphDatum = rawGraphData[graphDataCount++]
                                val prevVal = nameValueCalculation[times[timesIndex]] ?: 0F
                                nameValueCalculation[times[timesIndex]] = graphDatum.value + prevVal
                            }
                            timeIndexMap[times[timesIndex++]] = timePosition++
                        } else {
                            val graphDatum = rawGraphData[graphDataCount]
                            while (timesIndex < times.size && times[timesIndex] < graphDatum.time) {
                                timeIndexMap[times[timesIndex]] = timePosition++
                                nameValueCalculation[times[timesIndex++]] = 0F
                            }
                        }
                    }
//                  add missing times to a map (after)
                    while (timesIndex < times.size) {
                        timeIndexMap[times[timesIndex]] = timePosition++
                        nameValueCalculation[times[timesIndex++]] = 0F
                    }
                    if(times.isNotEmpty()) {
                        while (graphDataCount < rawGraphData.size) {
                            val graphDatum = rawGraphData[graphDataCount++]
                            val prevVal = nameValueCalculation[times[timesIndex - 1]] ?: 0F
                            nameValueCalculation[times[timesIndex - 1]] = graphDatum.value + prevVal
                        }
                    }
                }
            }
            nameValueCalculation.map { entry ->
                GraphData(name, entry.value)
            }
        } else {
            return when (calculation) {
                GraphCalculationType.Mean -> {
                    val freqForMean = mutableMapOf<Long, Int>()
                    val valueOfMean = mutableMapOf<Long, Float>()
                    while (timesIndex < times.size && times[timesIndex] < earliestDate) {
                        timeIndexMap[times[timesIndex]] = timePosition++
                        freqForMean[times[timesIndex]] = 1
                        valueOfMean[times[timesIndex++]] = 0F
                    }
//                  add the times from the first through the last data entry
                    while (timesIndex < times.size && graphDataCount < rawGraphData.size) {
                        if (rawGraphData[graphDataCount].time <= times[timesIndex]) {
                            while (graphDataCount < rawGraphData.size && rawGraphData[graphDataCount].time <= times[timesIndex]) {
                                val graphDatum = rawGraphData[graphDataCount++]
                                val time = times[timesIndex]
                                val prevFreq = freqForMean[time] ?: 0
                                val prevMean = valueOfMean[time] ?: 0F
                                freqForMean[time] = prevFreq + 1
                                valueOfMean[time] = prevMean + graphDatum.value
                            }
                            timeIndexMap[times[timesIndex++]] = timePosition++
                        } else {
                            val graphDatum = rawGraphData[graphDataCount]
                            while (times[timesIndex] < graphDatum.time) {
                                timeIndexMap[times[timesIndex]] = timePosition++
                                freqForMean[times[timesIndex]] = 1
                                valueOfMean[times[timesIndex++]] = 0F
                            }
                        }
                    }
//                  add missing times to a map (after)
                    while (timesIndex < times.size) {
                        timeIndexMap[times[timesIndex]] = timePosition++
                        freqForMean[times[timesIndex]] = 1
                        valueOfMean[times[timesIndex++]] = 0F
                    }

                    if(times.isNotEmpty()) {
                        while (graphDataCount < rawGraphData.size) {
                            val graphDatum = rawGraphData[graphDataCount++]
                            val time = times[timesIndex-1]
                            val prevFreq = freqForMean[time] ?: 0
                            val prevMean = valueOfMean[time] ?: 0F
                            freqForMean[time] = prevFreq + 1
                            valueOfMean[time] = prevMean + graphDatum.value
                        }
                    }
                    freqForMean.map { entry ->
                        val value = (valueOfMean[entry.key] ?: 0F) / entry.value
                        GraphData(name, value)
                    }
                }
                GraphCalculationType.Mode -> {
                    val listOfFrequencies = mutableListOf<MutableMap<Float, Int>>()
                    val maxFreqs = mutableListOf<Pair<Float, Int>>()
                    val multiFreq = mutableListOf<Boolean>()
                    var multiModes = 0

                    while (timesIndex < times.size && times[timesIndex] < earliestDate) {
                        timeIndexMap[times[timesIndex++]] = timePosition++
                        val map = mutableMapOf<Float, Int>()
                        map[0F] = 1
                        listOfFrequencies.add(map)
                        maxFreqs.add(Pair(0F, 1))
                        multiFreq.add(false)
                    }
//                  add the times from the first through the last data entry
                    while (timesIndex < times.size && graphDataCount < rawGraphData.size) {
//                        Time of the data item less than time in the time set
                        if (rawGraphData[graphDataCount].time <= times[timesIndex]) {
//                            data items left && data time is less than time in the time set
                            while (graphDataCount < rawGraphData.size && rawGraphData[graphDataCount].time <= times[timesIndex]) {
                                val graphDatum = rawGraphData[graphDataCount++]
                                val ind = timeIndexMap[times[timesIndex]]
                                if (ind == null) {
                                    timeIndexMap[times[timesIndex]] = timePosition
                                    val map = mutableMapOf<Float, Int>()
                                    map[graphDatum.value] = 1
                                    listOfFrequencies.add(map)
                                    maxFreqs.add(Pair(graphDatum.value, 1))
                                    multiFreq.add(false)
                                } else {
                                    val prevVal = listOfFrequencies[ind][graphDatum.value] ?: 0
                                    val newVal = prevVal + 1
                                    val max = maxFreqs[ind].second
                                    listOfFrequencies[ind][graphDatum.value] = newVal
                                    if (max <= newVal) {
                                        if (max == newVal) {
                                            multiFreq[ind] = true
                                            multiModes++
                                        } else {
                                            if (multiFreq[ind]) {
                                                multiFreq[ind] = false
                                                multiModes--
                                            }
                                            maxFreqs[ind] = Pair(graphDatum.value, newVal)
                                        }
                                    }
                                }
                            }
                            timeIndexMap[times[timesIndex++]] = timePosition++
                        } else {
                            val graphDatum = rawGraphData[graphDataCount]
                            while (timesIndex < times.size && times[timesIndex] < graphDatum.time) {
                                timeIndexMap[times[timesIndex++]] = timePosition++
                                val map = mutableMapOf<Float, Int>()
                                map[graphDatum.value] = 1
                                listOfFrequencies.add(map)
                                maxFreqs.add(Pair(0F, 1))
                                multiFreq.add(false)
                            }
                        }
                    }
//                  add missing times to a map (after)
                    while (timesIndex < times.size) {
                        timeIndexMap[times[timesIndex++]] = timePosition++
                        val map = mutableMapOf<Float, Int>()
                        map[0F] = 1
                        listOfFrequencies.add(map)
                        maxFreqs.add(Pair(0F, 1))
                        multiFreq.add(false)
                    }
                    if(times.isNotEmpty()) {
                        while (graphDataCount < rawGraphData.size) {
                            val graphDatum = rawGraphData[graphDataCount++]
                            val ind = timeIndexMap[times[timesIndex-1]]
                            if (ind == null) {
                                timeIndexMap[times[timesIndex-1]] = timePosition
                                val map = mutableMapOf<Float, Int>()
                                map[graphDatum.value] = 1
                                listOfFrequencies.add(map)
                                maxFreqs.add(Pair(graphDatum.value, 1))
                                multiFreq.add(false)
                            } else {
                                val prevVal = listOfFrequencies[ind][graphDatum.value] ?: 0
                                val newVal = prevVal + 1
                                val max = maxFreqs[ind].second
                                listOfFrequencies[ind][graphDatum.value] = newVal
                                if (max <= newVal) {
                                    if (max == newVal) {
                                        multiFreq[ind] = true
                                        multiModes++
                                    } else {
                                        if (multiFreq[ind]) {
                                            multiFreq[ind] = false
                                            multiModes--
                                        }
                                        maxFreqs[ind] = Pair(graphDatum.value, newVal)
                                    }
                                }
                            }
                        }
                    }
                    if (multiModes > 0) {
                        for (i in multiFreq.indices) {
                            if (multiFreq[i]) {
                                val maxFreq = maxFreqs[i].second
                                var average = 0F
                                var freqCount = 0
                                listOfFrequencies[i].forEach { entry ->
                                    if (entry.value == maxFreq) {
                                        average += entry.key
                                        freqCount++
                                    }
                                }
                                val mean = average / freqCount
                                //                            give negative mean to indicate averaged value- for later use
                                maxFreqs[i] = Pair(-mean, maxFreq)
                            }
                        }
                    }
                    timeIndexMap.map { entry ->
                        GraphData(name, maxFreqs[entry.value].first)
                    }
                    //                Median
                }
                else -> {
                    val values = mutableListOf<MutableList<Float>>()
                    var count = 0
                    while (timesIndex < times.size && times[timesIndex] < earliestDate) {
                        timeIndexMap[times[timesIndex++]] = timePosition++
                        values.add(mutableListOf())
                    }
//                  add the times from the first through the last data entry
                    while (timesIndex < times.size && graphDataCount < rawGraphData.size) {
                        if (rawGraphData[graphDataCount].time <= times[timesIndex]) {
                            while (graphDataCount < rawGraphData.size && rawGraphData[graphDataCount].time <= times[timesIndex]) {
                                val graphDatum = rawGraphData[graphDataCount++]
                                val ind = timeIndexMap[times[timesIndex]]
                                if (ind == null) {
                                    val list = mutableListOf(graphDatum.value)
                                    timeIndexMap[times[timesIndex]] = count++
                                    values.add(list)
                                } else {
                                    values[ind].add(graphDatum.value)
                                }
                            }
                            timeIndexMap[times[timesIndex++]] = timePosition++
                        } else {
                            val graphDatum = rawGraphData[graphDataCount]
                            while (timesIndex < times.size && times[timesIndex] < graphDatum.time) {
                                timeIndexMap[times[timesIndex++]] = timePosition++
                                values.add(mutableListOf())
                            }
                        }
                    }
//                  add missing times to a map (after)
                    while (timesIndex < times.size) {
                        timeIndexMap[times[timesIndex++]] = timePosition++
                        values.add(mutableListOf())
                    }

                    if(times.isNotEmpty()) {
                        while (graphDataCount < rawGraphData.size) {
                            val graphDatum = rawGraphData[graphDataCount++]
                            val ind = timeIndexMap[times[timesIndex-1]]
                            if (ind == null) {
                                val list = mutableListOf(graphDatum.value)
                                timeIndexMap[times[timesIndex-1]] = count++
                                values.add(list)
                            } else {
                                values[ind].add(graphDatum.value)
                            }
                        }
                    }
                    timeIndexMap.map { entry ->
                        val medianFinder = MedianFinder()
                        val medianList = values[entry.value]

                        if (medianList.isNotEmpty()) {
                            medianList.forEach { value ->
                                medianFinder.addNum(value)
                            }
                            GraphData(name, medianFinder.findMedian())
                        } else {
                            GraphData(name, 0F)
                        }
                    }
                }
            }
        }
    }

    fun makeCalculation(
        calculation: GraphCalculationType, rawGraphData: List<GraphData>
    ): List<GraphData> {

//        conditionally branch based on calculation because some calculations require all of the data
        return if (calculation == GraphCalculationType.Sum || calculation == GraphCalculationType.Min || calculation == GraphCalculationType.Max) {
            val nameValueCalculation = mutableMapOf<String, Float>()
//                Log.d("DATA VM", "${graphDatum.value}")
            when (calculation) {
                GraphCalculationType.Max -> {
                    rawGraphData.forEach { graphDatum ->
                        val prevVal = nameValueCalculation[graphDatum.name]
                        if ((prevVal ?: 0F) < graphDatum.value) {
                            nameValueCalculation[graphDatum.name] = graphDatum.value
                        }
                    }
                }
                GraphCalculationType.Min -> {
                    rawGraphData.forEach { graphDatum ->
                        val prevVal = nameValueCalculation[graphDatum.name]
                        if (prevVal == null || prevVal > graphDatum.value) {
                            nameValueCalculation[graphDatum.name] = graphDatum.value
                        }
                    }
                }
//                    SUM
                else -> {
                    rawGraphData.forEach { graphDatum ->
                        val prevVal = nameValueCalculation[graphDatum.name]
                        nameValueCalculation[graphDatum.name] = graphDatum.value + (prevVal ?: 0F)
                    }
                }
            }
            nameValueCalculation.map { entry ->
                GraphData(entry.key, entry.value)
            }
        } else {
            return when (calculation) {
                GraphCalculationType.Mean -> {
                    val freqForMean = mutableMapOf<String, Int>()
                    val valueOfMean = mutableMapOf<String, Float>()
                    rawGraphData.forEach { graphDatum ->

                        val prevFreq = freqForMean[graphDatum.name] ?: 0
                        val prevMean = valueOfMean[graphDatum.name] ?: 0F
                        freqForMean[graphDatum.name] = prevFreq + 1
                        valueOfMean[graphDatum.name] = prevMean + graphDatum.value
                    }
                    freqForMean.map { entry ->
                        val value = (valueOfMean[entry.key] ?: 0F) / entry.value
                        GraphData(entry.key, value)
                    }
                }
                GraphCalculationType.Mode -> {
                    val indexMap = mutableMapOf<String, Int>()
                    val listOfFrequencies = mutableListOf<MutableMap<Float, Int>>()
                    val maxFreqs = mutableListOf<Pair<Float, Int>>()
                    val multiFreq = mutableListOf<Boolean>()
                    var count = 0
                    var multiModes = 0

                    rawGraphData.forEach { graphDatum ->
                        val ind = indexMap[graphDatum.name]
                        if (ind == null) {
                            indexMap[graphDatum.name] = count++
                            val map = mutableMapOf<Float, Int>()
                            map[graphDatum.value] = 1
                            listOfFrequencies.add(map)
                            maxFreqs.add(Pair(graphDatum.value, 1))
                            multiFreq.add(false)
                        } else {
                            val prevVal = listOfFrequencies[ind][graphDatum.value] ?: 0
                            val newVal = prevVal + 1
                            val max = maxFreqs[ind].second
                            listOfFrequencies[ind][graphDatum.value] = newVal
                            if (max <= newVal) {
                                if (max == newVal) {
                                    multiFreq[ind] = true
                                    multiModes++
                                } else {
                                    if (multiFreq[ind]) {
                                        multiFreq[ind] = false
                                        multiModes--
                                    }
                                    maxFreqs[ind] = Pair(graphDatum.value, newVal)
                                }
                            }
                        }
                    }
                    if (multiModes > 0) {
                        for (i in multiFreq.indices) {
                            if (multiFreq[i]) {
                                val maxFreq = maxFreqs[i].second
                                var average = 0F
                                var freqCount = 0
                                listOfFrequencies[i].forEach { entry ->
                                    if (entry.value == maxFreq) {
                                        average += entry.key
                                        freqCount++
                                    }
                                }
                                val mean = average / freqCount
//                            give negative mean to indicate averaged value- for indicating averaged mode on graphscreen
                                maxFreqs[i] = Pair(-mean, maxFreq)
                            }
                        }
                    }
                    indexMap.map { entry ->
                        GraphData(entry.key, maxFreqs[entry.value].first)
                    }
                    //                Median
                }
                else -> {
                    val indexMap = mutableMapOf<String, Int>()
                    val values = mutableListOf<MutableList<Float>>()
                    var count = 0
                    rawGraphData.forEach { graphDatum ->
                        val ind = indexMap[graphDatum.name]
                        if (ind == null) {
                            val list = mutableListOf<Float>()
                            list.add(graphDatum.value)
                            indexMap[graphDatum.name] = count++
                            values.add(list)
                        } else {
                            values[ind].add(graphDatum.value)
                        }
                    }

                    indexMap.map { entry ->
                        val medianFinder = MedianFinder()
                        values[entry.value].forEach { value ->
                            medianFinder.addNum(value)
                        }
                        GraphData(entry.key, medianFinder.findMedian())
                    }
                }
            }
        }
    }
}