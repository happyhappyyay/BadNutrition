package com.happyhappyyay.badnutrition.ui.graph

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.ui.charts.*
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.chipRoundedShape
import com.happyhappyyay.badnutrition.ui.util.SelectionItem
import kotlinx.coroutines.launch

val dropDownMenuWidth = 125.dp

@Composable
fun GraphScreen(viewModel: GraphViewModel = viewModel()) {
    val uiState by viewModel.graphUiState.collectAsState()
    val graphData by viewModel.graphData.collectAsState()
    val editData by viewModel.editUiState.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    Log.d("GRAPH SCREEN", "recomposed")
    if (editData.isEditMode) {
        BackHandler {
            viewModel.resetEditSelection()
        }
    }
    Column {
        val graphOptions = GraphOptions(graphType = uiState.graphType,
            dataSize = viewModel.timesSize,
            selectedInd = uiState.selectedBar,
            isEditMode = editData.isEditMode,
            hasXAxisTicks = true,
            onEdit = { count, newEditVal ->
                viewModel.onEditSelection(count, newEditVal)
            },
            onZoom = { zoomAction ->
                coroutineScope.launch {
                    scrollState.scroll(MutatePriority.PreventUserInput) {
                        zoomAction()
                    }
                }
            })
        LazyColumn(
            state = scrollState, modifier = Modifier
                .background(MaterialTheme.colors.primary.copy(alpha = .3F))
                .padding(top = 50.dp)
                .fillMaxHeight()
        ) {
            item {
                Spacer(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                )
            }
//            TODO: animate removal of items opt-in or create list for animated visibility
            itemsIndexed(graphData) { count, data ->
                ChosenChart(
                    modifier = Modifier.weight(1f),
                    data = data,
                    count = count,
                    editable = editData.editItems[count],
                    graphOptions = graphOptions,
                    heading = "Summary of The Greatest Nutrients Ever!",
                    onEnlarge = {
                        coroutineScope.launch {
                            scrollState.animateScrollToItem(count + 1)
                        }
                    },
                    onSelectBar = {
                        viewModel.onBarSelected(it?.index ?: -1)
                    },
                ) {

                }
            }
            item {
                DonutChart()
            }
        }
    }

    AppBar(
        isEditMode = editData.isEditMode,
        clearEditCount = { viewModel.resetEditSelection() },
        onDeleteGraphData = { viewModel.onDeleteSelected() },
        onJoinGraphData = { viewModel.onMergeSelected() }
    ) { expanded ->
        GraphController(expanded = expanded,
            menuTitles = viewModel.createVisibleMenuItemsList(),
            menuItems = viewModel.nestedMenuSelection,
            onGraphTypeSelected = { type -> viewModel.onGraphTypeSelected(type) },
            onGraphCategorySelected = { category ->
                viewModel.onCategoryTypeSelected(
                    category
                )
            },
            onGraphCalculationSelected = { calculation ->
                viewModel.onCalculationTypeSelected(
                    calculation
                )
            },
            onGraphMeasurementSelected = { measurement ->
                viewModel.onGraphMeasurementSelected(
                    measurement
                )
            },
            onGraphOrderSelected = { order -> viewModel.onGraphOrderSelected(order) },
            onMenuSelected = { menu -> viewModel.onMenuSelected(menu) })
    }

    AnimatedVisibility(
        visible = uiState.selectionMenu != GraphSelectionType.None,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
    ) {
        ItemSelector(
            needsFilter = uiState.selectionMenu == GraphSelectionType.Foods,
            title = uiState.selectionMenu.name,
            things = list
        ) {
            viewModel.onMenuSelected(GraphSelectionType.None)
        }
    }

    AnimatedVisibility(visible = loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { }, contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.primary.copy(0.2F),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(8.dp)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun AppBar(
    isEditMode: Boolean,
    clearEditCount: () -> Unit,
    onJoinGraphData: () -> Unit,
    onDeleteGraphData: () -> Unit,
    expandedContent: @Composable (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primaryVariant)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    if (isEditMode) clearEditCount()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = ""
                    )
                }
                TextButton(enabled = !isEditMode, onClick = { expanded = !expanded }) {
                    Text(
                        if (isEditMode) "Edit Graphs" else "Graphs",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onPrimary
                    )
                    if (!isEditMode) {
                        Icon(
                            modifier = Modifier.padding(top = 4.dp),
                            imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = null
                        )
                    }
                }
                Row {
                    if (isEditMode) {
                        IconButton(onClick = { onJoinGraphData() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.join_black_24),
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = { onDeleteGraphData() }) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = null
                            )
                        }
                    } else {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
        expandedContent(expanded && !isEditMode)
    }
}

@Composable
fun GraphController(
    expanded: Boolean,
    menuTitles: List<String>,
    menuItems: List<List<String>>,
    onGraphTypeSelected: (GraphType) -> Unit,
    onGraphCategorySelected: (GraphCategoryType) -> Unit,
    onGraphCalculationSelected: (GraphCalculationType) -> Unit,
    onGraphMeasurementSelected: (GraphMeasurementType) -> Unit,
    onGraphOrderSelected: (GraphOrderType) -> Unit,
    onMenuSelected: (GraphSelectionType) -> Unit
) {
    AnimatedVisibility(
        visible = expanded, enter = expandVertically(), exit = shrinkVertically()
    ) {
        HorizontalMenu(menuTitles = menuTitles,
            menuItems = menuItems,
            selectionTitles = GraphSelectionType.values().mapIndexed { index, graphSelection ->
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
                    0 -> onGraphTypeSelected(GraphType.valueOf(selectedItem))
                    1 -> onGraphCategorySelected(
                        GraphCategoryType.valueOf(
                            selectedItem
                        )
                    )
                    2 -> onGraphCalculationSelected(
                        GraphCalculationType.valueOf(
                            selectedItem
                        )
                    )
                    3 -> onGraphMeasurementSelected(
                        GraphMeasurementType.valueOf(
                            selectedItem
                        )
                    )
                    4 -> onGraphOrderSelected(
                        GraphOrderType.valueOf(
                            selectedItem
                        )
                    )
                    else -> {}
                }
            },
            onSelectSelectionItem = { selectedItem ->
                onMenuSelected(GraphSelectionType.valueOf(selectedItem))
            })
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
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .background(color = MaterialTheme.colors.primaryVariant)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        menuTitles.forEachIndexed { ind, title ->
            HorizontalMenuDropChip(
                selectedText = title, menuItems = menuItems[ind]
            ) { selection ->
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
        Row(modifier = Modifier
            .height(32.dp)
            .clip(chipRoundedShape)
            .border(
                0.5.dp, Color.LightGray, chipRoundedShape
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
            .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRef),
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
            Text(
                title,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun HorizontalMenuDropChip(
    selectedText: String, menuItems: List<String>, onSelect: (String) -> Unit
) {
    var rememberIsSelected by rememberSaveable { mutableStateOf(false) }
    Box(modifier = Modifier.padding(horizontal = 8.dp)) {
        val baseModifier = Modifier
            .height(32.dp)
            .clip(chipRoundedShape)
        val chipModifier = if (rememberIsSelected) baseModifier.background(
            Color(
                android.graphics.Color.parseColor(
                    "#E8DEF8"
                )
            )
        ) else baseModifier.border(
            0.5.dp,
            color = if (rememberIsSelected) MaterialTheme.colors.onBackground else Color.LightGray,
            chipRoundedShape
        )
        Row(modifier = chipModifier
            .clickable { rememberIsSelected = true }
            .padding(horizontal = 8.dp)
            .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically) {
            if (rememberIsSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.round_check_black_20),
                    contentDescription = "",
                )
            }
            Text(
                selectedText,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = if (rememberIsSelected) MaterialTheme.colors.onBackground else MaterialTheme.colors.onPrimary
            )
            Icon(
                painter = painterResource(id = if (rememberIsSelected) R.drawable.round_arrow_drop_up_black_24 else R.drawable.rounded_arrow_drop_down_black_24),
                contentDescription = "",
                tint = if (rememberIsSelected) MaterialTheme.colors.onBackground else MaterialTheme.colors.onPrimary
            )
        }
        GraphDropDownMenu(menuItems = menuItems,
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
    modifier: Modifier = Modifier, text: String, enabled: Boolean = true, onClick: () -> Unit
) {
    DropdownMenuItem(modifier = modifier
        .width(dropDownMenuWidth)
        .background(
            color = if (enabled) Color.Transparent else MaterialTheme.colors.primary.copy(
                alpha = .15f
            )
        ), enabled = enabled, onClick = {
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
    count: Int,
    data: List<GraphData> = emptyList(),
    heading: String = "Summary",
    graphOptions: GraphOptions,
    editable: Boolean,
    onEnlarge: () -> Unit,
    onSelectBar: (BarArea?) -> Unit,
    menuContent: @Composable () -> Unit
) {
    when (graphOptions.graphType) {
        GraphType.Bar -> {
            GraphAttempt6(modifier,
                count = count,
                list = data.ifEmpty { emptyList() },
                graphOptions = graphOptions,
                isEditSelected = editable,
                onEnlarge = onEnlarge,
                onSelected = { bar -> onSelectBar(bar) }) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        heading,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "January 25th, 1921",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onBackground.copy(alpha = .5f)
                    )
                }
            }
        }
        GraphType.Line -> {
            GraphAttempt6(modifier,
                graphOptions = graphOptions,
                list = data,
                count = count,
                isEditSelected = editable,
                onEnlarge = onEnlarge,
                onSelected = { bar -> onSelectBar(bar) }) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
                    Text(
                        data[0].name,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
        }
        GraphType.Scatter -> {
            GraphAttempt6(modifier,
                graphOptions = graphOptions,
                list = data,
                count = count,
                isEditSelected = editable,
                onEnlarge = onEnlarge,
                onSelected = { bar -> onSelectBar(bar) }) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
                    Text(
                        heading, fontSize = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewGraphScreen() {
    BadNutritionTheme {
        GraphScreen()
    }
}
