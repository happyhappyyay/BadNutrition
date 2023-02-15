package com.happyhappyyay.badnutrition.graph

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.charts.*
import com.happyhappyyay.badnutrition.charts.GraphOptions
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.chipRoundedShape
import com.happyhappyyay.badnutrition.util.SelectionItem
import com.happyhappyyay.badnutrition.util.SimpleRadioButton
import kotlinx.coroutines.launch

enum class None {
    None
}

enum class GraphSelectionType {
    Foods, Nutrients, Partitions, Time, None
}

enum class CategoryType {
    Food, Nutrient, Partition, Time
}

enum class GraphFilterType {
    Chart, Group, Result, Measure, Order
}

enum class GraphMeasurementType {
    Amount, Entries, Exists, Percent, Serving
}

enum class GraphCalculationType {
    Max, Mean, Median, Min, Mode, Sum
}

enum class GraphOrderType {
    None,
    Largest,
    Smallest
}

val dropDownMenuWidth = 125.dp

fun createNestedDropdownList(): List<List<String>> {
    return listOf(
        GraphType.values().map { it.name },
        CategoryType.values().map { it.name },
        GraphCalculationType.values().map { it.name },
        GraphMeasurementType.values().map { it.name },
        GraphOrderType.values().map { it.name },
    )
}

@Composable
fun GraphScreen() {
    var isFullScreen by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val rememberScrollState = rememberLazyListState()
    var rememberGraphType by rememberSaveable { mutableStateOf(GraphType.Scatter) }
    var rememberGroupType by rememberSaveable { mutableStateOf(CategoryType.Nutrient) }
    var rememberXAxisType by rememberSaveable { mutableStateOf(CategoryType.Time) }
    var rememberCalculationType by rememberSaveable { mutableStateOf(GraphCalculationType.Mean) }
    var rememberMeasureType by rememberSaveable { mutableStateOf(GraphMeasurementType.Percent) }
    var rememberOrderType by rememberSaveable { mutableStateOf(GraphOrderType.None) }
    var includeEmptyEntries by rememberSaveable { mutableStateOf(false) }
    var includeValueRange by rememberSaveable { mutableStateOf(false) }
    var minimumValue by rememberSaveable { mutableStateOf(0) }
    var maximumValue by rememberSaveable { mutableStateOf(0) }
    var rememberEditCount by rememberSaveable { mutableStateOf(0) }
    val rememberEditMode by remember { derivedStateOf { rememberEditCount > 0 } }
    var rememberZoomMode by rememberSaveable { mutableStateOf(ZoomDistanceOption.Day) }
    var rememberSelectionItem by rememberSaveable { mutableStateOf(GraphSelectionType.None) }
    var selectedBar by rememberSaveable { mutableStateOf(-1) }

    Log.d("GRAPH SCREEN", "recomposed")
    var rememberTop by remember { mutableStateOf(4) }

    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
        if(rememberEditMode) {
            BackHandler {
                rememberEditCount = 0
            }
        }
        Box(modifier = Modifier
            .background(MaterialTheme.colors.primaryVariant)
            .fillMaxWidth()
        ){
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, tint = MaterialTheme.colors.onPrimary, contentDescription = "")
                }
                Text(if(rememberEditMode) "Edit Graphs" else "Graphs", style = MaterialTheme.typography.h6, color = MaterialTheme.colors.onPrimary)
                Row{
                    if(rememberEditMode) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(painter = painterResource(id = R.drawable.join_black_24), tint = MaterialTheme.colors.onPrimary, contentDescription = null)
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Rounded.Delete, tint = MaterialTheme.colors.onPrimary,contentDescription = null)
                        }
                        IconButton(onClick = { rememberEditCount = 0 }) {
                            Icon(imageVector = Icons.Rounded.Clear, tint = MaterialTheme.colors.onPrimary, contentDescription = null)
                        }
                    }
                    else {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
        if (rememberTop == 1) {
            ControlBox(
                Modifier,
                selectedChart = rememberGraphType,
                onSelectChart = { selected ->
                    rememberGraphType = selected
                },
                expandControl = {
                    isFullScreen = true
                },
                onSelectCategory = { selected ->
                    rememberGroupType = selected
                },
                selectedCategory = rememberGroupType,
                onSelectMetric = { selected ->
                    rememberMeasureType = selected
                },
                selectedMetric = rememberMeasureType,
                onSelectEntries = {
                    includeEmptyEntries = !includeEmptyEntries
                },
                selectedEntries = includeEmptyEntries,
                onSelectOrder = { selected ->
                    rememberOrderType = selected
                },
                selectedOrder = rememberOrderType
            ) {
                rememberSelectionItem = rememberSelectionItem
            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.primary.copy(.3f))
        ) {
            val graphOptions = GraphOptions(
                graphType = rememberGraphType,
                selectedInd = selectedBar,
                editMode = rememberEditMode,
                axisTicks = true,
                onEdit = { count -> rememberEditCount += count },
                onZoom = { zoomAction ->
                    coroutineScope.launch {
                        rememberScrollState.scroll(MutatePriority.PreventUserInput) {
                            zoomAction()
                        }
                    }
                }
            )
            Column {
                HorizontalMenu(
                    menuTitles = listOf(
                        rememberGraphType.name,
                        rememberGroupType.name,
                        rememberCalculationType.name,
                        rememberMeasureType.name,
                        rememberOrderType.name
                    ),
                    menuItems = createNestedDropdownList(),
                    selectionTitles = GraphSelectionType.values()
                        .mapIndexed { index, graphSelection ->
                            val iconResource = when (index) {
                                0 -> R.drawable.round_restaurant_black_20
                                1 -> R.drawable.round_nutrition_black_20
                                2 -> R.drawable.round_stack_black_20
                                3 -> R.drawable.round_date_range_black_20
                                else -> R.drawable.round_list_alt_black_24
                            }
                            SelectionItem(graphSelection.name, iconResource)
                        },
                    onSelectMenuItem = { ind, selectedItem ->
                        when (ind) {
                            0 -> rememberGraphType = GraphType.valueOf(selectedItem)
                            1 -> rememberGroupType = CategoryType.valueOf(selectedItem)
                            2 -> rememberCalculationType =
                                GraphCalculationType.valueOf(selectedItem)
                            3 -> rememberMeasureType =
                                GraphMeasurementType.valueOf(selectedItem)
                            4 -> rememberOrderType =
                                GraphOrderType.valueOf(selectedItem)
                            else -> {}
                        }
                    },
                    onSelectSelectionItem = { selectedItem ->
                        rememberSelectionItem = GraphSelectionType.valueOf(selectedItem)
                    }
                )
                LazyColumn(
                    state = rememberScrollState,
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    items(100) { count ->
                        ChosenChart(
                            modifier = Modifier
                                .weight(1f),
//                .clickable { rememberTop = rememberTop % 4 + 1 },
                            data = MockData().nutrientItems,
                            graphOptions = graphOptions,
                            heading = "Summary of The Greatest Nutrients Ever!",
                            onEnlarge = {
                                coroutineScope.launch {
                                    rememberScrollState.animateScrollToItem(count)
                                    Log.d("GRAPHSCREEN", "$count")
                                }
                            },
                            onSelectBar = {
                                selectedBar = it?.index ?: -1
                            },
                        ) {
                            if (rememberTop == 4) {
                                HorizontalMenu(
                                    menuTitles = listOf(
                                        rememberGraphType.name,
                                        rememberGroupType.name,
                                        rememberCalculationType.name,
                                        rememberMeasureType.name,
                                        rememberOrderType.name
                                    ),
                                    menuItems = createNestedDropdownList(),
                                    selectionTitles = GraphSelectionType.values()
                                        .mapIndexed { index, graphSelection ->
                                            val iconResource = when (index) {
                                                0 -> R.drawable.round_restaurant_black_20
                                                1 -> R.drawable.round_nutrition_black_20
                                                2 -> R.drawable.round_stack_black_20
                                                3 -> R.drawable.round_date_range_black_20
                                                else -> R.drawable.round_list_alt_black_24
                                            }
                                            SelectionItem(graphSelection.name, iconResource)
                                        },
                                    onSelectMenuItem = { ind, selectedItem ->
                                        when (ind) {
                                            0 -> rememberGraphType = GraphType.valueOf(selectedItem)
                                            1 -> rememberGroupType =
                                                CategoryType.valueOf(selectedItem)
                                            2 -> rememberCalculationType =
                                                GraphCalculationType.valueOf(selectedItem)
                                            3 -> rememberMeasureType =
                                                GraphMeasurementType.valueOf(selectedItem)
                                            4 -> rememberOrderType =
                                                GraphOrderType.valueOf(selectedItem)
                                            else -> {}
                                        }
                                    },
                                    onSelectSelectionItem = { selectedItem ->
                                        rememberSelectionItem =
                                            GraphSelectionType.valueOf(selectedItem)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (rememberTop == 2) {
            ControlBox(
                Modifier,
                selectedChart = rememberGraphType,
                onSelectChart = { selected ->
                    rememberGraphType = selected
                },
                expandControl = {
                    isFullScreen = true
                },
                onSelectCategory = { selected ->
                    rememberGroupType = selected
                },
                selectedCategory = rememberGroupType,
                onSelectMetric = { selected ->
                    rememberMeasureType = selected
                },
                selectedMetric = rememberMeasureType,
                onSelectEntries = {
                    includeEmptyEntries = !includeEmptyEntries
                },
                selectedEntries = includeEmptyEntries,
                onSelectOrder = { selected ->
                    rememberOrderType = selected
                },
                selectedOrder = rememberOrderType
            ) {
                rememberSelectionItem = rememberSelectionItem
            }
        }
    }
    if (rememberTop == 1) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1F))
            NestedDropDownBox(
                nestedDropdownTitles = GraphFilterType.values().map { it.name },
                nestedDropdownItems = createNestedDropdownList(),
                selectedDropDownItems = listOf(
                    rememberGraphType.name,
                    rememberGroupType.name,
                    rememberMeasureType.name,
                    rememberMeasureType.name,
                    rememberMeasureType.name
                )
            ) { ind, selectedItem ->
                run {
                    when (ind) {
                        0 -> rememberGraphType = GraphType.valueOf(selectedItem)
                        1 -> rememberGroupType = CategoryType.valueOf(selectedItem)
                        2 -> rememberMeasureType =
                            GraphMeasurementType.valueOf(selectedItem)
                    }
                }
            }
        }
    }
    AnimatedVisibility(
        visible = rememberSelectionItem != GraphSelectionType.None,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
    ) {
        ItemSelector(
            needsFilter = rememberSelectionItem == GraphSelectionType.Foods,
            title = rememberSelectionItem.name,
            things = list
        ) {
            rememberSelectionItem = GraphSelectionType.None
        }
    }
}

@Composable
fun NestedDropDownBox(
    nestedDropdownTitles: List<String>,
    nestedDropdownItems: List<List<String>>,
    selectedDropDownItems: List<String>,
    onSelectDropDownItem: (Int, String) -> Unit
) {
    var rememberIndExpanded by rememberSaveable { mutableStateOf(-1) }
    var rememberIsExpanded by rememberSaveable { mutableStateOf(false) }
    val xOffsetNestedDropdown =
        LocalConfiguration.current.screenWidthDp.dp - (dropDownMenuWidth + dropDownMenuWidth) + 1.dp

    Box {
        IconButton(
            modifier = Modifier.width(IntrinsicSize.Min),
            onClick = { rememberIsExpanded = !rememberIsExpanded }) {
            Icon(
                painter = painterResource(id = R.drawable.round_filter_list_black_24),
                contentDescription = ""
            )
        }
        DropdownMenu(
            expanded = rememberIsExpanded,
            onDismissRequest = { rememberIsExpanded = false }) {
            Box(
                modifier = Modifier.offset(
                    x = xOffsetNestedDropdown,
                )
            ) {
                val isNestedExpanded = rememberIndExpanded != -1
                DropdownMenu(
                    expanded = isNestedExpanded,
                    onDismissRequest = { rememberIndExpanded = -1 }) {
                    if (isNestedExpanded) {
                        val selectionOfDropdownItems =
                            nestedDropdownItems[rememberIndExpanded]
                        val selectedDropDownItem =
                            selectedDropDownItems[rememberIndExpanded]
                        selectionOfDropdownItems.forEach { dropDownItemName ->
                            val isSelected = selectedDropDownItem == dropDownItemName
                            DropdownMenuItem(modifier = Modifier.width(dropDownMenuWidth),
                                contentPadding = PaddingValues(horizontal = 8.dp),
                                enabled = !isSelected,
                                onClick = {
                                    onSelectDropDownItem(
                                        rememberIndExpanded,
                                        dropDownItemName
                                    )
                                    rememberIndExpanded = -1
                                    rememberIsExpanded = false
                                }) {
                                Icon(
                                    modifier = Modifier.padding(
                                        top = 6.dp,
                                        bottom = 4.dp,
                                        end = 8.dp
                                    ),
                                    painter = painterResource(id = R.drawable.round_check_black_20),
                                    tint = if (isSelected) MaterialTheme.colors.onSurface else Color.Transparent,
                                    contentDescription = ""
                                )

                                Text(
                                    modifier = Modifier.padding(),
                                    text = dropDownItemName,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }
                        }
                    }
                }
            }
            nestedDropdownTitles.forEachIndexed { i, dropDownItemName ->
                val isSelectedItem = rememberIndExpanded == i
                DropdownMenuItem(
                    modifier = Modifier
                        .width(dropDownMenuWidth)
                        .background(
                            if (isSelectedItem) MaterialTheme.colors.primary.copy(
                                .12F
                            ) else Color.Transparent
                        ),
                    enabled = !isSelectedItem,
                    contentPadding = PaddingValues(horizontal = 6.dp),
                    onClick = {
                        rememberIndExpanded = i
                    },
                ) {
                    Icon(
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, end = 6.dp),
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        tint = MaterialTheme.colors.onSurface,
                        contentDescription = ""
                    )
                    Text(
                        text = dropDownItemName,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1
                    )

                }
            }
        }
    }
}

@Composable
fun HorizontalMenu(
    menuTitles: List<String>,
    menuItems: List<List<String>>,
    selectionTitles: List<SelectionItem>,
    onSelectMenuItem: (Int, String) -> Unit,
    onSelectSelectionItem: (String) -> Unit
) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState()).background(color = MaterialTheme.colors.primary.copy(.3F))) {
        menuTitles.forEachIndexed { ind, title ->
            HorizontalMenuDropChip(selectedText = title, menuItems = menuItems[ind]) { selection ->
                onSelectMenuItem(ind, selection)
            }
        }
        selectionTitles.forEach { title ->
            HorizontalMenuSelectionChip(title = title.title, title.iconId) {
                onSelectSelectionItem(title.title)
            }
        }
    }
}

@Composable
fun HorizontalMenuSelectionChip(title: String, iconRef: Int, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(
            modifier = Modifier
                .height(32.dp)
                .clip(chipRoundedShape)
                .border(
                    0.5.dp,
                    MaterialTheme.colors.onBackground,
                    chipRoundedShape
                )
                .clickable { onClick() }
                .padding(horizontal = 8.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRef),
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
            Text(title, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}

@Composable
fun HorizontalMenuDropChip(
    selectedText: String,
    menuItems: List<String>,
    onSelect: (String) -> Unit
) {
    var rememberIsSelected by rememberSaveable { mutableStateOf(false) }
    Box(modifier = Modifier.padding(horizontal = 8.dp)) {
        val baseModifier = Modifier
            .height(32.dp)
            .clip(chipRoundedShape)
        val chipModifier =
            if (rememberIsSelected) baseModifier.background(
                Color(
                    android.graphics.Color.parseColor(
                        "#E8DEF8"
                    )
                )
            ) else baseModifier.border(
                0.5.dp,
                MaterialTheme.colors.onBackground,
                chipRoundedShape
            )
        Row(
            modifier = chipModifier
                .clickable { rememberIsSelected = true }
                .padding(horizontal = 8.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (rememberIsSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.round_check_black_20),
                    contentDescription = "",
                )
            }
            Text(
                selectedText,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Icon(
                painter = painterResource(id = if (rememberIsSelected) R.drawable.round_arrow_drop_up_black_24 else R.drawable.rounded_arrow_drop_down_black_24),
                contentDescription = ""
            )
        }
        GraphDropDownMenu(
            menuItems = menuItems,
            selectedItem = selectedText,
            expanded = rememberIsSelected,
            onDismiss = { rememberIsSelected = false }) { selection ->
            onSelect(selection)
            rememberIsSelected = false
        }
    }
}

@Composable
fun GraphDropDownMenu(
    menuItems: List<String>,
    selectedItem: String,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = { onDismiss() }) {
        menuItems.forEach { menuItemName ->
            val isSelected = menuItemName == selectedItem
            GraphTextMenuItem(text = menuItemName, enabled = !isSelected) {
                onSelect(menuItemName)
            }
        }
    }
}

@Composable
fun GraphTextMenuItem(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    DropdownMenuItem(modifier = modifier
        .width(dropDownMenuWidth)
        .background(
            color = if (enabled) Color.Transparent else MaterialTheme.colors.primary.copy(
                alpha = .15f
            )
        ),
        enabled = enabled,
        onClick = {
            onClick()
        }) {
        Text(
            text = text,
            color = MaterialTheme.colors.onSurface,
        )
    }
}

@Composable
fun ChosenChart(
    modifier: Modifier = Modifier,
    data: Array<Float>,
    heading: String = "Summary",
    graphOptions: GraphOptions,
    onEnlarge: () -> Unit,
    onSelectBar: (BarArea?) -> Unit,
    menuContent: @Composable () -> Unit
) {
    when (graphOptions.graphType) {
        GraphType.Bar -> {
            GraphAttempt6(
                modifier,
                graphOptions = graphOptions,
                onEnlarge = onEnlarge,
                onSelected = { bar -> onSelectBar(bar) }) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        "Nutrients",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        "January 25th, 1921",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onBackground.copy(alpha = .5f)
                    )
                }
                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
//                    menuContent()
                }
            }
        }
        GraphType.Line -> {
            GraphAttempt6(
                modifier,
                graphOptions = graphOptions,
                onEnlarge = onEnlarge,
                onSelected = { bar -> onSelectBar(bar) }) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
                    Text(
                        "Nutrients",
                        fontSize = 18.sp
                    )
//                    Text(
//                        "January 25th, 1921",
//                        style = MaterialTheme.typography.subtitle2,
//                        color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
//                    )
                }
                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
//                    menuContent()
                }
            }
        }
        GraphType.Scatter -> {
            GraphAttempt6(
                modifier,
                graphOptions = graphOptions,
                onEnlarge = onEnlarge,
                onSelected = { bar -> onSelectBar(bar) }) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
                    Text(
                        "Nutrients",
                        fontSize = 18.sp
                    )
                }
                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
//                    menuContent()
                }
            }
        }
    }
}

@Composable
fun ControlBox(
    modifier: Modifier = Modifier,
    selectedChart: GraphType,
    onSelectChart: (GraphType) -> Unit,
    expandControl: () -> Unit,
    onSelectCategory: (CategoryType) -> Unit,
    selectedCategory: CategoryType,
    onSelectMetric: (GraphMeasurementType) -> Unit,
    selectedMetric: GraphMeasurementType,
    onSelectEntries: () -> Unit,
    selectedEntries: Boolean,
    selectedOrder: GraphOrderType,
    onSelectOrder: (GraphOrderType) -> Unit,
    graphChartSelection: () -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(1) {
            GraphDropDown(
                title = "Chart",
                selectedItem = selectedChart,
                items = GraphType.values(),
                selectItem = onSelectChart
            )
            GraphDropDown(
                title = "Grouping",
                selectedItem = selectedCategory,
                items = CategoryType.values(),
                selectItem = onSelectCategory
            )
            GraphDropDown(
                title = "Measurement",
                selectedItem = selectedMetric,
                items = GraphMeasurementType.values(),
                selectItem = onSelectMetric
            )
            GraphDropDown(title = "Calculation",
                selectedItem = GraphCalculationType.Mean,
                items = GraphCalculationType.values(),
                selectItem = { }
            )
            GraphDropDown(
                title = "Order",
                selectedItem = selectedOrder,
                items = GraphOrderType.values(),
                selectItem = onSelectOrder
            )
        }
        items(1) {
            GraphChartSelectionForm("Foods") { graphChartSelection() }
            GraphChartSelectionForm("Nutrients") { graphChartSelection() }
            GraphChartSelectionForm("Partitions") { graphChartSelection() }
            GraphChartSelectionForm("Time Span") { graphChartSelection() }
        }
    }
}

@Composable
fun <T : Enum<T>> GraphDropDown(
    selectedItem: T,
    items: Array<T>,
    selectItem: (T) -> Unit,
    title: String,
) {
    var rememberIsExpanded by remember { mutableStateOf(false) }
    Box {
        TextButton(modifier = Modifier
            .width(150.dp),
            onClick = { rememberIsExpanded = !rememberIsExpanded }) {
            Text(
                text = title,
            )
        }
        DropdownMenu(
            modifier = Modifier
                .background(MaterialTheme.colors.surface),
            expanded = rememberIsExpanded,
            onDismissRequest = { rememberIsExpanded = !rememberIsExpanded }) {
            items.forEach { type ->
                val isSelected = type == selectedItem
                DropdownMenuItem(modifier = Modifier
                    .width(150.dp)
                    .background(color = if (isSelected) MaterialTheme.colors.surface.copy(.12F) else MaterialTheme.colors.surface),
                    onClick = {
                        rememberIsExpanded = !rememberIsExpanded
                        selectItem(type)
                    }) {
                    if (isSelected) {
                        Icon(
                            modifier = Modifier.padding(end = 12.dp),
                            imageVector = Icons.Rounded.Check,
                            contentDescription = ""
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = if (isSelected) 0.dp else 36.dp),
                        text = type.name,
                        color = MaterialTheme.colors.onSurface,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun <T : Enum<T>> GraphDropDownTop(
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    selectedItem: T,
    items: Array<T>,
    selectItem: (T) -> Unit,
    title: String,
) {
    var rememberIsExpanded by remember { mutableStateOf(false) }
    val x = offset.x
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { rememberIsExpanded = !rememberIsExpanded }
                .background(if (rememberIsExpanded) MaterialTheme.colors.primary.copy(.12F) else Color.Transparent)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                tint = MaterialTheme.colors.onSurface,
                contentDescription = ""
            )
            Text(
                text = title,
                color = MaterialTheme.colors.onSurface,
            )
        }
        Box(modifier = Modifier.offset(x)) {
            DropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface),
                expanded = rememberIsExpanded,
                onDismissRequest = { rememberIsExpanded = !rememberIsExpanded }) {
                items.forEach { type ->
                    val isSelected = type == selectedItem
                    DropdownMenuItem(modifier = Modifier
                        .width(150.dp),
                        enabled = !isSelected,
                        onClick = {
                            selectItem(type)
                            rememberIsExpanded = !rememberIsExpanded
                        }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    modifier = Modifier.padding(end = 12.dp),
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = ""
                                )
                            }
                            Text(
                                modifier = Modifier.padding(start = if (isSelected) 0.dp else 36.dp),
                                text = type.name,
                                color = MaterialTheme.colors.onSurface,
                                overflow = TextOverflow.Ellipsis
                            )

                        }

                    }
                }
            }
        }
    }
}

@Composable
fun <T : Enum<T>> GraphDropDownTopSplit(
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    selectedItem: T,
    items: Array<T>,
    selectItem: (T) -> Unit,
    selectedItem2: T,
    selectItem2: (T) -> Unit,
    isSecondEnabled: Boolean,
    title: String,
) {
    var rememberIsExpanded by remember { mutableStateOf(false) }
    val x = offset.x - 100.dp
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { rememberIsExpanded = !rememberIsExpanded }
                .background(if (rememberIsExpanded) MaterialTheme.colors.primary.copy(.12F) else Color.Transparent)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                tint = MaterialTheme.colors.onSurface,
                contentDescription = ""
            )
            Text(
                text = title,
                color = MaterialTheme.colors.onSurface,
            )
        }
        Box(modifier = Modifier.offset(x)) {
            DropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface),
                expanded = rememberIsExpanded,
                onDismissRequest = { rememberIsExpanded = !rememberIsExpanded }) {
                for (i in 0 until items.size / 2) {
                    val type = items[i]
                    val isSelected = type == selectedItem
                    DropdownMenuItem(modifier = Modifier
                        .width(150.dp),
                        enabled = !isSelected,
                        onClick = {
                            selectItem(type)
                            rememberIsExpanded = !rememberIsExpanded
                        }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    modifier = Modifier.padding(end = 12.dp),
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = ""
                                )
                            }
                            Text(
                                modifier = Modifier.padding(start = if (isSelected) 0.dp else 36.dp),
                                text = type.name,
                                color = MaterialTheme.colors.onSurface,
                                overflow = TextOverflow.Ellipsis
                            )

                        }

                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                for (i in items.size / 2 until items.size) {
                    val type = items[i]
                    val isSelected = type == selectedItem2
                    DropdownMenuItem(modifier = Modifier
                        .width(150.dp),
                        enabled = isSecondEnabled && !isSelected,
                        onClick = {
                            selectItem2(type)
                            rememberIsExpanded = !rememberIsExpanded
                        }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSecondEnabled && isSelected) {
                                Icon(
                                    modifier = Modifier.padding(end = 12.dp),
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = ""
                                )
                            }
                            Text(
                                modifier = Modifier.padding(start = if (isSecondEnabled && isSelected) 0.dp else 36.dp),
                                text = type.name,
                                color = if (isSecondEnabled) MaterialTheme.colors.onSurface else Color.Gray,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GraphChartSelectionForm(title: String, filterItems: (String) -> Unit) {
    Text(text = title, modifier = Modifier
        .width(150.dp)
        .clickable { filterItems(title) }
        .padding(12.dp),
        overflow = TextOverflow.Ellipsis)
}

@Preview
@Composable
fun PreviewSimpleRadioButton() {
    BadNutritionTheme {
        SimpleRadioButton(label = "Bar Chart", isEnabled = true, onClick = { })
    }
}

@Preview
@Composable
fun PreviewGraphScreen() {
    BadNutritionTheme {
        GraphScreen()
    }
}

@Preview
@Composable
fun PreviewControlBox() {
    BadNutritionTheme {
        ControlBox(
            modifier = Modifier,
            GraphType.Bar,
            {},
            {},
            {},
            CategoryType.Food,
            {},
            GraphMeasurementType.Exists,
            { },
            true,
            GraphOrderType.None,
            {}
        ) {}
    }
}

@Preview
@Composable
fun PreviewChartTypeRadioForm() {
    BadNutritionTheme {
        GraphDropDown(GraphType.Bar, GraphType.values(), {}, "Chart Type")
    }
}