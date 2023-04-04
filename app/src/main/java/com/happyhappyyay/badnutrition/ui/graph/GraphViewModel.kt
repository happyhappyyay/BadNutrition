package com.happyhappyyay.badnutrition.ui.graph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happyhappyyay.badnutrition.data.createNutrientMap
import com.happyhappyyay.badnutrition.data.createRandomFoodList
import com.happyhappyyay.badnutrition.data.createTimeList
import com.happyhappyyay.badnutrition.data.food.PortionOfFood
import com.happyhappyyay.badnutrition.data.nutrient.NutrientInfo
import com.happyhappyyay.badnutrition.ui.charts.GraphData
import com.happyhappyyay.badnutrition.ui.charts.GraphDataFilterOptions
import com.happyhappyyay.badnutrition.ui.charts.GraphType
import com.happyhappyyay.badnutrition.ui.charts.ZoomDistanceOption
import com.happyhappyyay.badnutrition.ui.util.GraphDataFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GraphViewModel : ViewModel() {
    private val nutrients = createNutrientMap()
    private val originalFoodPortions = emptyList<PortionOfFood>()
    private val originalNutrients = emptyList<NutrientInfo>()
    private var updatedFoodPortions = listOf<PortionOfFood>()
    private val selectedCalculationType = MutableStateFlow(GraphCalculationType.Mean)
    private val selectedCategoryType = MutableStateFlow(GraphCategoryType.Nutrient)
    private val selectedGraphType = MutableStateFlow(GraphType.Line)
    private val selectedMeasurementType = MutableStateFlow(GraphMeasurementType.Amount)
    private val selectedMenu = MutableStateFlow(GraphSelectionType.None)
    private val selectedOrderType = MutableStateFlow(GraphOrderType.None)
    private val selectedBarInd = MutableStateFlow(-1)
    private val selectedZoom = MutableStateFlow(ZoomDistanceOption.Day)
    private var unselectedFoods = setOf<Long>()
    private var unselectedNutrients = setOf<Long>()
    private var unselectedTimes = setOf<Long>()
    private var unselectedPartitions = setOf<Long>()
    private var editCount = 0

    val nestedMenuSelection: List<List<String>> = listOf(
        GraphType.values().map { it.name },
        GraphCategoryType.values().map { it.name },
        GraphCalculationType.values().map { it.name },
        GraphMeasurementType.values().map { it.name },
        GraphOrderType.values().map { it.name },
    )

    private val _times = createTimeList()
    val timesSize: Int
    get() = _times.size


    private var _editedSize = 0
    val editedSize: Int
    get() = _editedSize

    private var _mergedDataItems = mutableListOf<IntArray>()
    val mergedDataItems: List<IntArray>
    get() = _mergedDataItems

    private val _graphData = MutableStateFlow(emptyList<List<GraphData>>())
    val graphData: StateFlow<List<List<GraphData>>>
        get() = _graphData

    private val _graphUiState = MutableStateFlow(GraphUiState())
    val graphUiState: StateFlow<GraphUiState>
        get() = _graphUiState

    private val _editUiState = MutableStateFlow(EditUiState())
    val editUiState: StateFlow<EditUiState>
        get() = _editUiState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean>
        get() = _loading

    init {
        _loading.value = true
        viewModelScope.launch {
            combine(
                selectedCalculationType,
                selectedCategoryType,
                selectedGraphType,
                selectedMeasurementType,
                selectedOrderType,
                selectedBarInd,
                selectedMenu,
                selectedZoom
            ) {
                GraphUiState(
                    calculationType = it[0] as GraphCalculationType,
                    categoryType = it[1] as GraphCategoryType,
                    graphType = it[2] as GraphType,
                    measurementType = it[3] as GraphMeasurementType,
                    orderType = it[4] as GraphOrderType,
                    selectedBar = it[5] as Int,
                    selectionMenu = it[6] as GraphSelectionType,
                    zoomMode = it[7] as ZoomDistanceOption
                )
            }
//                .catch { throwable ->
//                // TODO: emit a UI error here. For now we'll just rethrow
//                throw throwable
//            }
                .collect {
                    _graphUiState.value = it
                }

        }
        updatedFoodPortions = createRandomFoodList()
        updateDataList()
    }

    private fun updateDataList() {
        viewModelScope.launch {
            _loading.value = true
            updateData()
            _loading.value = false
        }
    }

    private suspend fun updateData() = withContext(Dispatchers.Default) {
        val graphFilterOptions = GraphDataFilterOptions(
            selectedGraphType.value,
            selectedCategoryType.value,
            selectedCalculationType.value,
            selectedMeasurementType.value,
            selectedOrderType.value
        )
        val dataFilter = GraphDataFilter(graphFilterOptions, updatedFoodPortions, nutrients, _times)
        _graphData.update {
            dataFilter.createGraphList()
        }
        _editedSize = _graphData.value.size
        resetEditSelection()
    }

    private fun createDataList(
        foods: Set<Long> = emptySet(),
        nutrients: Set<Long> = emptySet(),
        partitions: Set<Long> = emptySet(),
        times: Set<Long> = emptySet()
    ) {
        if (foods.isNotEmpty()) {
            updatedFoodPortions = updatedFoodPortions.filter { portion ->
                !foods.contains(portion.food.id)
            }
            unselectedFoods = foods
        }
        if (nutrients.isNotEmpty()) {
            unselectedNutrients = nutrients
        }
        if (partitions.isNotEmpty()) {
            updatedFoodPortions = updatedFoodPortions.filter { portion ->
                !partitions.contains(portion.portion.partitionId)
            }
            unselectedPartitions = partitions
        }
        if (times.isNotEmpty()) {
            updatedFoodPortions = updatedFoodPortions.filter { portion ->
                !times.contains(portion.portion.dateMs)
            }
            unselectedTimes = times
        }
        updateDataList()
    }

    fun onBarSelected(barInd: Int) {
        selectedBarInd.value = barInd
    }

    fun onEditSelection(editInd: Int, isSelected: Boolean) {
        editCount += if (isSelected) 1 else -1
        if (editCount <= 0) {
            _editUiState.update {
                val list =
                    it.editItems.mapIndexed { index, b -> if (index == editInd) isSelected else b }
                it.copy(isEditMode = false, editItems = list)
            }
        } else {
            if (editCount == 1 && isSelected) {
                _editUiState.update {
                    val list =
                        it.editItems.mapIndexed { index, b -> if (index == editInd) isSelected else b }
                    it.copy(isEditMode = true, editItems = list)
                }
            } else {
                _editUiState.update {
                    val list =
                        it.editItems.mapIndexed { index, b -> if (index == editInd) isSelected else b }
                    it.copy(editItems = list)
                }
            }
        }

    }

    fun onDeleteSelected() {
        _graphData.update {
            it.filterIndexed { ind, _ -> !_editUiState.value.editItems[ind] }
        }
        resetEditSelection()
    }

    fun onMergeSelected() {
        if (editCount > 1) {
            val set = mutableSetOf<Int>()
            var firstItemInd = -1
            val editItemLen = editUiState.value.editItems.size-1
            for (i in 0..editItemLen) {
                if (editUiState.value.editItems[i]) {
                    firstItemInd = i
                    break
                }
            }
            if(firstItemInd > -1) {
                val masterMergerData = mutableListOf<GraphData>()
                for (i in firstItemInd..editItemLen) {
                    if(editUiState.value.editItems[i]){
                        set.add(i)
                        masterMergerData.addAll(_graphData.value[i])
                    }
                }
                _graphData.update {
                    val data = mutableListOf<List<GraphData>>()
                    for(i in it.indices) {
                        if(!set.contains(i)){
                            data.add(it[i])
                        }
                    }
                    data.add(firstItemInd,masterMergerData)
                    data
                }
            }
            resetEditSelection()
        }
    }

    fun resetEditSelection() {
        editCount = 0
        _editUiState.update {
            it.copy(isEditMode = false, editItems = List(_graphData.value.size) { false })
        }
    }

    fun onCalculationTypeSelected(calculation: GraphCalculationType) {
        if (calculation != selectedCalculationType.value) {
            resetSelectedIndex()
            selectedCalculationType.value = calculation
            updateDataList()
        }
    }

    fun onCategoryTypeSelected(category: GraphCategoryType) {
        if (category != selectedCategoryType.value) {
            resetSelectedIndex()
            selectedCategoryType.value = category
            updateDataList()
        }
    }

    fun updateFoodSelection(removalSet: Set<Long>) {
        createDataList(foods = removalSet)
    }

    fun updateNutrientSelection(removalSet: Set<Long>) {
        createDataList(nutrients = removalSet)
    }

    fun updateTimeSelection(removalSet: Set<Long>) {
        createDataList(times = removalSet)
    }

    fun updatePartitionSelection(removalSet: Set<Long>) {
        createDataList(partitions = removalSet)
    }

    fun onGraphTypeSelected(type: GraphType) {
        if (type != selectedGraphType.value) {
            resetSelectedIndex()
            selectedGraphType.value = type
            updateDataList()
        }
    }

    fun onGraphMeasurementSelected(measurement: GraphMeasurementType) {
        if (measurement != selectedMeasurementType.value) {
            resetSelectedIndex()
            selectedMeasurementType.value = measurement
            updateDataList()
        }
    }

    fun onGraphOrderSelected(order: GraphOrderType) {
        selectedOrderType.value = order
    }

    fun onMenuSelected(menu: GraphSelectionType) {
        selectedMenu.value = menu
    }

    fun onZoomSelected(zoom: ZoomDistanceOption) {
        selectedZoom.value = zoom
    }

    private fun resetSelectedIndex() {
        selectedBarInd.value = -1
    }

    fun createVisibleMenuItemsList(): List<String> {
        return listOf(
            selectedGraphType.value.name,
            selectedCategoryType.value.name,
            selectedCalculationType.value.name,
            selectedMeasurementType.value.name,
            selectedOrderType.value.name
        )
    }
}
