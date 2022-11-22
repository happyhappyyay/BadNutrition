package com.happyhappyyay.badnutrition.graph

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.home.horGestureThreshold
import com.happyhappyyay.badnutrition.home.nutrientItems
import com.happyhappyyay.badnutrition.home.unitItems
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.Shapes
import com.happyhappyyay.badnutrition.util.ScrollButton
import com.happyhappyyay.badnutrition.util.adjustTransparency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

val list = arrayListOf(
    "Apple",
    "ho-oh",
    "Twinkie",
    "Carrot",
    "Chicken Wing",
    "a really long item that has a lot of text to go along with the longness of it",
    "Silver Dollar",
    "Cupcake",
    "banana",
    "cheese",
    "onion",
    "pesticide",
    "lettuce",
    "alojarte",
    "tuna",
    "berenjena",
    "Dongo"
).toTypedArray()

val filters = arrayListOf(
    "Protein > 10%", "Metabolic Rate < 94%", "Calcium > 55%", "Calcium < 78%"
).toTypedArray()

val sorts = arrayListOf(
    "Increasing Alphabetical",
    "Decreasing Calories",
    "Increasing Date Entered",
    "Decreasing Date Created",
    "increasing Frequency"
)

enum class ListModification {
    Filter,
    Sort
}

@Composable
fun ItemSelector(
    things: Array<String>, title: String, needsFilter: Boolean, onDismiss: () -> Unit
) {
    var direction = 0
    var isFiltered by rememberSaveable { mutableStateOf(false) }
    var searchItem by rememberSaveable { mutableStateOf("") }
    var isShowingDialog by rememberSaveable { mutableStateOf(false) }
    var isFilterExpanded by remember { mutableStateOf(true) }
    var isSortExpanded by remember { mutableStateOf(true) }
//    no need to change list just list items. Is it just a list and not mutable list
    val nutrientAmounts =
        remember { mutableStateListOf<Boolean>().apply { addAll(Array(things.size) { true }) } }
    val filterItems = remember { mutableStateListOf<String>().apply { addAll(filters) } }
    val sortItems = remember { mutableStateListOf<String>().apply { addAll(sorts) } }
    val listState = rememberLazyListState()
    var showSort by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    AnimatedVisibility(visible = isShowingDialog) {

        Dialog(onDismissRequest = { isShowingDialog = !isShowingDialog }) {
            if (showSort) {
                AddSort(nutrients = nutrientItems, onAdd = { item ->
                    sortItems.add(item)
                    isShowingDialog = !isShowingDialog
                }
                ) {
                    isShowingDialog = !isShowingDialog
                }
            } else {
                AddFilter(nutrients = nutrientItems, measurements = unitItems, onAdd = { filter ->
                    filterItems.add(filter.toString())
                    isShowingDialog = !isShowingDialog
                }) {
                    isShowingDialog = !isShowingDialog
                }
            }
        }
    }
    Card {
        Column(
            Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consumeAllChanges()
                            val (_, y) = dragAmount
                            if (abs(y) > 30) {
                                direction = if (y > 0) -1 else 1
                            }
                        },
                        onDragEnd = {
                            if (direction < 0) {
                                onDismiss()
                            }
                        })
                }) {
                Column {
                    ItemSelectorHeading(searchItem = searchItem,
                        onDismiss = { onDismiss() },
                        onValueChange = { newVal -> searchItem = newVal })
                    ItemSelectorSubHeading(title = title, needsFilter) {
                        isFiltered = !isFiltered
                    }
                }
            }
            Box(modifier = Modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consumeAllChanges()
                            val (x, y) = dragAmount
                            if (abs(x) - horGestureThreshold - 10 > abs(y)) {
                                direction = if (x > 0) -1 else 1
                            }
                        },
                        onDragEnd = {
                            if ((direction > 0 && isFiltered) || (direction < 0 && !isFiltered)) {
                                isFiltered = !isFiltered
                            }
                        })
                }) {
                Column {
                    Row {
                        AnimatedVisibility(visible = isFiltered) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(.4f)
                                    .background(
                                        color = MaterialTheme.colors.secondaryVariant.adjustTransparency(
                                            .2F
                                        )
                                    )
                            ) {
                                items(1) {
                                    ItemSelectionParameters(parameters = sortItems,
                                        type = ListModification.Sort,
                                        resetParameters = { sortItems.clear() },
                                        addItem = {
                                            isShowingDialog = true
                                            showSort = true
                                        },
                                        removeItem = { sort -> sortItems.remove(sort) },
                                        isExpanded = isSortExpanded,
                                        updateExpanded = { isSortExpanded = !isSortExpanded }
                                    )
                                    ItemSelectionParameters(parameters = filterItems,
                                        type = ListModification.Filter,
                                        resetParameters = { filterItems.clear() },
                                        addItem = {
                                            isShowingDialog = true
                                            showSort = false
                                        },
                                        removeItem = { filter -> filterItems.remove(filter) },
                                        isExpanded = isFilterExpanded,
                                        updateExpanded = { isFilterExpanded = !isFilterExpanded }
                                    )
                                }
                            }
                        }
                        Column(verticalArrangement = Arrangement.SpaceEvenly) {
                            ItemSelectorSelectionColumn(
                                modifier = Modifier.weight(1f),
                                things,
                                nutrientAmounts,
                                listState,
                                showButton,
                                coroutineScope
                            )
                            ItemSelectorFooter(onClick = onDismiss)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemSelectionParameters(
    parameters: SnapshotStateList<String>,
    resetParameters: () -> Unit,
    isExpanded: Boolean,
    type: ListModification,
    updateExpanded: () -> Unit,
    addItem: () -> Unit,
    removeItem: (String) -> Unit
) {
    Row {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.clickable { updateExpanded() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (type == ListModification.Filter) "Filter by" else "Sort by",
                        modifier = Modifier.padding(8.dp)
                    )
                    Icon(
                        imageVector = if (!isExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        contentDescription = ""
                    )
                }
                IconButton(onClick = { addItem() }) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
                }
            }
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    parameters.forEach { param ->
                        FilterItem(filter = param) {
                            removeItem(param)
                        }

                    }
                    TextButton(onClick = { resetParameters() }) {
                        Text("Reset")
                    }
                }
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp), color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
fun SortItem(sort: String, removeItem: () -> Unit) {

}

@Composable
fun ItemSelectionFilters(
    filters: SnapshotStateList<String>,
    resetFilters: () -> Unit,
    addItem: () -> Unit,
    removeItem: (String) -> Unit
) {
    Text("${filters.size}")
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(.4f)
            .background(color = MaterialTheme.colors.secondaryVariant.adjustTransparency(.2F))
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Filter by", modifier = Modifier.padding(8.dp))
                IconButton(onClick = { addItem() }) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
                }
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                items(filters) { filter ->
                    FilterItem(filter = filter) {
                        removeItem(it)
                    }
                }
            }
            TextButton(onClick = { resetFilters() }) {
                Text("Reset")
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp), color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
fun FilterItem(filter: String, removeItem: (String) -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        onClick = { removeItem(filter) }) {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = filter,
                style = MaterialTheme.typography.caption
            )
            Icon(
                modifier = Modifier.weight(.2f),
                imageVector = Icons.Rounded.Clear,
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun ItemSelectorSelectionColumn(
    modifier: Modifier,
    things: Array<String>,
    nutrientAmounts: SnapshotStateList<Boolean>,
    listState: LazyListState,
    showButton: Boolean,
    coroutineScope: CoroutineScope
) {
    Box(modifier = modifier) {
        LazyColumn(
            state = listState
        ) {
            itemsIndexed(things) { i, thing ->
                ItemRow(name = thing,
                    isSelected = nutrientAmounts[i],
                    onSelect = { nutrientAmounts[i] = !nutrientAmounts[i] })
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )
            }
        }
        AnimatedVisibility(
            visible = showButton
        ) {
            ScrollButton {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        }
    }
}

@Composable
fun ItemSelectorHeading(
    modifier: Modifier = Modifier,
    searchItem: String,
    onDismiss: () -> Unit,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isSearching = searchItem != ""
    val hintColor = MaterialTheme.colors.onBackground.adjustTransparency(.5f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .padding(8.dp)
            .height(40.dp),
    ) {
        BasicTextField(modifier = modifier.fillMaxWidth(),
            value = searchItem,
            onValueChange = { newVal -> onValueChange(newVal) },
            singleLine = true,
            textStyle = MaterialTheme.typography.body1.copy(MaterialTheme.colors.onBackground),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() },
            ),
            decorationBox = { innerTextField ->
                Row {
                    IconButton(modifier = Modifier.weight(.2f), onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colors.background,
                        shape = CircleShape,
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search, contentDescription = ""
                            )
                            Box(modifier = Modifier.fillMaxWidth(.9F)) {
                                if (!isSearching) {
                                    Text(
                                        text = "Search for...",
                                        color = hintColor
                                    )
                                }
                                innerTextField()
                            }
                            if (isSearching) {
                                IconButton(onClick = {
                                    onValueChange("")
                                    focusManager.clearFocus()
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = "",
                                        tint = MaterialTheme.colors.onBackground
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.fillMaxWidth(.1f))
                }
            })
    }
}

@Composable
fun ItemSelectorFooter(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button({ onClick() }) {
            Text("CANCEL")
        }
        Button({ onClick() }) {
            Text("OK")
        }
    }
}

@Composable
fun ItemSelectorSubHeading(title: String, needsFilter: Boolean, onClick: () -> Unit) {
    Box(Modifier.background(MaterialTheme.colors.primary)) {
        Text(
            text = title,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary
        )
        if (needsFilter) {
            IconButton(onClick = { onClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.round_filter_list_black_24),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Composable
fun ItemRow(name: String, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(if (isSelected) MaterialTheme.colors.secondary.adjustTransparency(.2f) else Color.Transparent)
            .height(50.dp)
            .clickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colors.secondary else Color.Transparent)
                .border(
                    1.dp,
                    if (!isSelected) MaterialTheme.colors.onBackground else Color.Transparent,
                    shape = CircleShape
                )
                .fillMaxWidth(.25f)
                .padding(8.dp), contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(isSelected) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "")
            }
        }
        Text(
            name,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 8.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )

    }
}

@Preview
@Composable
fun ItemRowPreview() {
    BadNutritionTheme {
        Column {
            ItemRow(name = "Dionyses", isSelected = false) {}
            ItemRow(name = "Dionyses", isSelected = true) {}
        }
    }
}

@Preview
@Composable
fun ItemSelectorPreview() {
    BadNutritionTheme {
        ItemSelector(things = list, "Foods", true) {}
    }
}

@Preview
@Composable
fun ItemSelectorHeadingPreview() {
    BadNutritionTheme {
        ItemSelectorHeading(searchItem = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            onDismiss = {},
            onValueChange = {})
    }
}

@Preview
@Composable
fun ItemSelectorFilterPreview() {
    BadNutritionTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Row {
                ItemSelectionFilters(filters = remember {
                    mutableStateListOf<String>().apply {
                        addAll(
                            filters
                        )
                    }
                }, resetFilters = { }, addItem = { }, removeItem = {})
                Card(modifier = Modifier.fillMaxSize()) {

                }
            }
        }
    }
}