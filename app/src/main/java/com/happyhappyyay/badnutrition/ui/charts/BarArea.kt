package com.happyhappyyay.badnutrition.ui.charts

data class BarArea(
    val index: Int,
    val data: GraphData,
    val xStart: Float,
    val xEnd: Float,
    val yStart: Float,
    val yEnd: Float
)

data class GraphData(
    val name: String,
    val value: Float
)
