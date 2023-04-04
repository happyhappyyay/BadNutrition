package com.happyhappyyay.badnutrition.ui.graph

import androidx.compose.foundation.lazy.LazyListState
import com.happyhappyyay.badnutrition.ui.charts.GraphType
import com.happyhappyyay.badnutrition.ui.charts.ZoomDistanceOption

enum class GraphSelectionType {
    Foods, Nutrients, Partitions, Time, None
}

enum class GraphCategoryType {
    Food, Nutrient, Partition, Time
}

enum class GraphMeasurementType {
    Amount, Entries, Percent, Serving
}

enum class GraphCalculationType {
    Max, Mean, Median, Min, Mode, Sum
}

enum class GraphOrderType {
    None, Largest, Smallest
}

data class GraphUiState(
    val calculationType: GraphCalculationType = GraphCalculationType.Mean,
    val categoryType: GraphCategoryType = GraphCategoryType.Nutrient,
    val graphType: GraphType = GraphType.Bar,
    val measurementType: GraphMeasurementType = GraphMeasurementType.Percent,
    val orderType: GraphOrderType = GraphOrderType.None,
    val selectedBar: Int = -1,
    val selectionMenu: GraphSelectionType = GraphSelectionType.None,
    val zoomMode: ZoomDistanceOption = ZoomDistanceOption.Day,
)

data class EditUiState(
    val isEditMode: Boolean = false,
    val editItems: List<Boolean> = mutableListOf()
    )
