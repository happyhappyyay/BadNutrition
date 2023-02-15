package com.happyhappyyay.badnutrition.charts

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class GraphOptions(
    val graphType: GraphType,
    val zoomDistance: ZoomDistanceOption = ZoomDistanceOption.Day,
    val editMode: Boolean = false,
    val selectedInd: Int = -1,
    val heightDp: Dp = 175.dp,
    val axisTicks: Boolean = false,
    val individualZoom: Boolean = false,
    val individualSelection: Boolean = false,
    val straightLines: Boolean = false,
    val noFillLineGraph: Boolean = false,
    val lineOfBestFit: Boolean = true,
    var graphId: Int = 0,
    val onEdit: (Int) -> Unit = {},
    val onSelectData: (Int) -> Unit = {},
    val onZoom: (()->Unit) -> Unit = {},
)
