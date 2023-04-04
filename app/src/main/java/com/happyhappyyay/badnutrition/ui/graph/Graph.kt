package com.happyhappyyay.badnutrition.ui.graph

import com.happyhappyyay.badnutrition.ui.charts.GraphData

data class Graph(val names: Array<String>, val data: List<List<GraphData>>, val isSelected: Boolean)