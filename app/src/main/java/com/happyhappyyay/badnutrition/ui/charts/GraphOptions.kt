package com.happyhappyyay.badnutrition.ui.charts

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class GraphOptions(
    val isEditMode: Boolean = false,
    val dataSize: Int = 0,
    var graphId: Int = 0,
    val graphType: GraphType,
    val heightDp: Dp = 175.dp,
    val hasXAxisTicks: Boolean = false,
    val hasIndividualZoom: Boolean = false,
    val hasIndividualSelection: Boolean = false,
    val hasStraightLines: Boolean = false,
    val hasNoFillLineGraph: Boolean = false,
    val hasLineOfBestFit: Boolean = false,
    val hasFoodShapeSelectors: Boolean = false,
    val selectedInd: Int = -1,
    val zoomDistance: ZoomDistanceOption = ZoomDistanceOption.Day,
    val onEdit: (Int, Boolean) -> Unit = {_,_ -> },
    val onSelectData: (Int) -> Unit = {},
    val onZoom: (()->Unit) -> Unit = {},
)
