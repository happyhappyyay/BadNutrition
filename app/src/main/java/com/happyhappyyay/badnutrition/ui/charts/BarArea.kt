package com.happyhappyyay.badnutrition.ui.charts

import com.happyhappyyay.badnutrition.ui.graph.GraphCalculationType
import com.happyhappyyay.badnutrition.ui.graph.GraphCategoryType
import com.happyhappyyay.badnutrition.ui.graph.GraphMeasurementType
import com.happyhappyyay.badnutrition.ui.graph.GraphOrderType

data class BarArea(
    val index: Int,
    val data: GraphData,
    val xStart: Float,
    val xEnd: Float,
    val yStart: Float,
    val yEnd: Float
)

data class GraphData(
    val name: String, val value: Float
)

data class GraphDataTime(
    val name: String, val value: Float, val time: Long
)

data class GraphDataFilterOptions(
    val type: GraphType,
    val category: GraphCategoryType,
    val calculation: GraphCalculationType,
    val measure: GraphMeasurementType,
    val order: GraphOrderType
)
