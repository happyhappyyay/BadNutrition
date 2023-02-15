package com.happyhappyyay.badnutrition.ui.charts

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class GraphOptions(
    val graphType: GraphType,
    val zoomDistance: ZoomDistanceOption = ZoomDistanceOption.Day,
    val isEditMode: Boolean = false,
    val selectedInd: Int = -1,
    val heightDp: Dp = 175.dp,
    val hasXAxisTicks: Boolean = false,
    val hasIndividualZoom: Boolean = false,
    val hasIndividualSelection: Boolean = false,
    val hasStraightLines: Boolean = false,
    val hasNoFillLineGraph: Boolean = false,
    val hasLineOfBestFit: Boolean = true,
    val hasFoodShapeSelectors: Boolean = false,
    var graphId: Int = 0,
    val onEdit: (Int) -> Unit = {},
    val onSelectData: (Int) -> Unit = {},
    val onZoom: (()->Unit) -> Unit = {},
)
