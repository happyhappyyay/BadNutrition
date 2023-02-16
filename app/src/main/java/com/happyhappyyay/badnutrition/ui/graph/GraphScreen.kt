package com.happyhappyyay.badnutrition.ui.graph

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.data.gList
import com.happyhappyyay.badnutrition.ui.charts.*
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.chipRoundedShape
import com.happyhappyyay.badnutrition.util.SelectionItem
import kotlinx.coroutines.launch

enum class GraphSelectionType {
    Foods, Nutrients, Partitions, Time, None
}

enum class CategoryType {
    Food, Nutrient, Partition, Time
}

enum class GraphMeasurementType {
    Amount, Entries, Exists, Percent, Serving
}

enum class GraphCalculationType {
    Max, Mean, Median, Min, Mode, Sum
}

enum class GraphOrderType {
    None, Largest, Smallest
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
    val coroutineScope = rememberCoroutineScope()
    val rememberScrollState = rememberLazyListState()
    var rememberGraphType by rememberSaveable { mutableStateOf(GraphType.Scatter) }
    var rememberGroupType by rememberSaveable { mutableStateOf(CategoryType.Nutrient) }
    var rememberCalculationType by rememberSaveable { mutableStateOf(GraphCalculationType.Mean) }
    var rememberMeasureType by rememberSaveable { mutableStateOf(GraphMeasurementType.Percent) }
    var rememberOrderType by rememberSaveable { mutableStateOf(GraphOrderType.None) }
    var rememberEditCount by rememberSaveable { mutableStateOf(0) }
    val rememberEditMode by remember { derivedStateOf { rememberEditCount > 0 } }
    var rememberIsControlVisible by remember { mutableStateOf(true) }
    var rememberZoomMode by rememberSaveable { mutableStateOf(ZoomDistanceOption.Day) }
    var rememberIsGraphMenuDropped by remember { mutableStateOf(false) }
    var rememberSelectionItem by rememberSaveable { mutableStateOf(GraphSelectionType.None) }
    var selectedBar by rememberSaveable { mutableStateOf(-1) }
    val isControlVisible = rememberIsControlVisible && !rememberEditMode

    Log.d("GRAPH SCREEN", "recomposed")
    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
        if (rememberEditMode) {
            BackHandler {
                rememberEditCount = 0
            }
        }
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
                    if(rememberEditMode) rememberEditCount = 0
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = ""
                    )
                }
                TextButton(enabled = !rememberEditMode, onClick = { rememberIsControlVisible = !rememberIsControlVisible }) {
                    Text(
                        if (rememberEditMode) "Edit Graphs" else "Graphs",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onPrimary
                    )
                    if(!rememberEditMode){
                        Icon(
                            modifier = Modifier.padding(top = 4.dp),
                            imageVector = if(rememberIsControlVisible) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = null
                        )
                    }
                }
                Row {
                    if (rememberEditMode) {
                        IconButton(onClick = { /*TODO Join items in the list*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.join_black_24),
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = { /*TODO Remove items from List*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = null
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            rememberIsGraphMenuDropped = !rememberIsGraphMenuDropped
                        }) {
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

        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            val graphOptions = GraphOptions(graphType = rememberGraphType,
                selectedInd = selectedBar,
                isEditMode = rememberEditMode,
                hasXAxisTicks = true,
                onEdit = { count -> rememberEditCount += count },
                onZoom = { zoomAction ->
                    coroutineScope.launch {
                        rememberScrollState.scroll(MutatePriority.PreventUserInput) {
                            zoomAction()
                        }
                    }
                })
            Column {
                AnimatedVisibility(visible = isControlVisible) {
                    HorizontalMenu(menuTitles = listOf(
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
                                4 -> rememberOrderType = GraphOrderType.valueOf(selectedItem)
                                else -> {}
                            }
                        },
                        onSelectSelectionItem = { selectedItem ->
                            rememberSelectionItem = GraphSelectionType.valueOf(selectedItem)
                        })
                }
                LazyColumn(
                    state = rememberScrollState, modifier = Modifier.fillMaxHeight()
                ) {
                    items(10) { count ->
                        ChosenChart(
                            modifier = Modifier.weight(1f),
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

                        }
                    }
                    item {
                        DonutChart()
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
    data: Array<Float> = emptyArray(),
    heading: String = "Summary",
    graphOptions: GraphOptions,
    onEnlarge: () -> Unit,
    onSelectBar: (BarArea?) -> Unit,
    menuContent: @Composable () -> Unit
) {
    when (graphOptions.graphType) {
        GraphType.Bar -> {
            GraphAttempt6(modifier,
                graphOptions = graphOptions,
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
                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
//                    menuContent()
                }
            }
        }
        GraphType.Line -> {
            GraphAttempt6(modifier,
                graphOptions = graphOptions,
                onEnlarge = onEnlarge,
                onSelected = { bar -> onSelectBar(bar) }) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
                    Text(
                        heading, fontSize = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )

                }
                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
//                    menuContent()
                }
            }
        }
        GraphType.Scatter -> {
            GraphAttempt6(modifier,
                graphOptions = graphOptions,
                onEnlarge = onEnlarge,
                onSelected = { bar -> onSelectBar(bar) }) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
                    Text(
                        heading, fontSize = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
//                    menuContent()
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
