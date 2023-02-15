package com.happyhappyyay.badnutrition.graph
//
//import android.util.Log
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.slideInVertically
//import androidx.compose.animation.slideOutVertically
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.layout.boundsInRoot
//import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.DpOffset
//import androidx.compose.ui.unit.dp
//import com.happyhappyyay.badnutrition.R
//import com.happyhappyyay.badnutrition.charts.*
//import com.happyhappyyay.badnutrition.data.MockData
//
//
//@Composable
//fun GraphScreen1() {
//    var isFullScreen by rememberSaveable { mutableStateOf(false) }
//    var graphType by rememberSaveable { mutableStateOf(GraphType.Bar) }
//    var rememberGroupType by rememberSaveable { mutableStateOf(CategoryType.Nutrient) }
//    var rememberXAxisType by rememberSaveable { mutableStateOf(CategoryType.Time) }
//    var graphMeasurementType by rememberSaveable { mutableStateOf(GraphMeasurementType.Percent) }
//    var orderType by rememberSaveable { mutableStateOf(GraphOrderType.None) }
//    var includeEmptyEntries by rememberSaveable { mutableStateOf(false) }
//    var includeValueRange by rememberSaveable { mutableStateOf(false) }
//    var minimumValue by rememberSaveable { mutableStateOf(0) }
//    var maximumValue by rememberSaveable { mutableStateOf(0) }
//    var isShowingSelection by rememberSaveable { mutableStateOf(false) }
//    var selectedBar by rememberSaveable { mutableStateOf(-1) }
//
//    Log.d("GRAPH SCREEN", "recomposed")
//    var rememberTop by remember { mutableStateOf(3) }
//    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
//        if (rememberTop == 1) {
//            ControlBox(
//                Modifier,
//                selectedChart = graphType,
//                onSelectChart = { selected ->
//                    graphType = selected
//                },
//                expandControl = {
//                    isFullScreen = true
//                },
//                onSelectCategory = { selected ->
//                    rememberGroupType = selected
//                },
//                selectedCategory = rememberGroupType,
//                onSelectMetric = { selected ->
//                    graphMeasurementType = selected
//                },
//                selectedMetric = graphMeasurementType,
//                onSelectEntries = {
//                    includeEmptyEntries = !includeEmptyEntries
//                },
//                selectedEntries = includeEmptyEntries,
//                onSelectOrder = { selected ->
//                    orderType = selected
//                },
//                selectedOrder = orderType
//            ) {
//                isShowingSelection = !isShowingSelection
//            }
//        }
//        ChosenChart(
//            modifier = Modifier
//                .weight(1f)
//                .clickable { rememberTop = rememberTop % 3 + 1 },
//            type = graphType,
//            data = MockData().nutrientItems,
//            heading = "Summary of The Greatest Nutrients Ever!",
//            selectedBar = selectedBar,
//        ) {
//            selectedBar = it?.index ?: -1
//        }
//        if (rememberTop == 2) {
//            ControlBox(
//                Modifier,
//                selectedChart = graphType,
//                onSelectChart = { selected ->
//                    graphType = selected
//                },
//                expandControl = {
//                    isFullScreen = true
//                },
//                onSelectCategory = { selected ->
//                    rememberGroupType = selected
//                },
//                selectedCategory = rememberGroupType,
//                onSelectMetric = { selected ->
//                    graphMeasurementType = selected
//                },
//                selectedMetric = graphMeasurementType,
//                onSelectEntries = {
//                    includeEmptyEntries = !includeEmptyEntries
//                },
//                selectedEntries = includeEmptyEntries,
//                onSelectOrder = { selected ->
//                    orderType = selected
//                },
//                selectedOrder = orderType
//            ) {
//                isShowingSelection = !isShowingSelection
//            }
//        }
//    }
//    if (rememberTop == 3) {
//        var rememberDrop by remember { mutableStateOf(false) }
//        var position = Offset(0F, 0F)
//        Row(modifier = Modifier.fillMaxWidth()) {
//            Spacer(modifier = Modifier.weight(1F))
//            Box(
//                modifier = Modifier
//                    .onGloballyPositioned {
//                        val rectBounds = it.boundsInRoot()
//                        position = rectBounds.topLeft
//                    }
//                    .width(IntrinsicSize.Min),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                IconButton(
//                    modifier = Modifier.width(IntrinsicSize.Min),
//                    onClick = { rememberDrop = !rememberDrop }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.round_filter_list_black_24),
//                        contentDescription = ""
//                    )
//                }
//                DropdownMenu(
//                    modifier = Modifier.background(MaterialTheme.colors.surface),
//                    expanded = rememberDrop,
//                    onDismissRequest = { rememberDrop = !rememberDrop }) {
//                    val x = with(LocalDensity.current) { position.x.toDp() }
//                    val y = with(LocalDensity.current) { position.y.toDp() }
//                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
//                        GraphDropDownTop(
//                            offset = DpOffset(x, y),
//                            title = "Chart",
//                            selectedItem = graphType,
//                            items = GraphType.values(),
//                            selectItem = { type ->
//                                rememberDrop = !rememberDrop
//                                graphType = type
//                            }
//                        )
//                    }
//                    GraphDropDownTopSplit(
//                        offset = DpOffset(x, y),
//                        title = "Grouping",
//                        selectedItem = rememberGroupType,
//                        items = CategoryType.values(),
//                        selectItem = { type ->
//                            rememberGroupType = type
//                            rememberDrop = !rememberDrop
//                        },
//                        selectedItem2 = rememberXAxisType,
//                        selectItem2 = { type ->
//                            rememberXAxisType = type
//                            rememberDrop = !rememberDrop
//                        },
//                        isSecondEnabled = graphType != GraphType.Bar
//                    )
//                    GraphDropDownTop(
//                        offset = DpOffset(x, y),
//                        title = "Measure",
//                        selectedItem = graphMeasurementType,
//                        items = GraphMeasurementType.values(),
//                        selectItem = { type ->
//                            graphMeasurementType = type
//                            rememberDrop = !rememberDrop
//                        }
//                    )
//                    GraphDropDownTop(
//                        offset = DpOffset(x, y),
//                        title = "Calculation",
//                        selectedItem = GraphCalculationType.Mean,
//                        items = GraphCalculationType.values(),
//                        selectItem = { rememberDrop = !rememberDrop }
//                    )
//                    GraphDropDownTop(
//                        offset = DpOffset(x, y),
//                        title = "Order",
//                        selectedItem = orderType,
//                        items = GraphOrderType.values(),
//                        selectItem = { type ->
//                            orderType = type
//                            rememberDrop = !rememberDrop
//                        }
//                    )
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//                    GraphChartSelectionForm("Foods") {
//                        rememberDrop = !rememberDrop
//                        isShowingSelection = true
//                    }
//                    GraphChartSelectionForm("Nutrients") { }
//                    GraphChartSelectionForm("Partitions") {}
//                    GraphChartSelectionForm("Time Span") { }
//                    GraphChartSelectionForm("Settings") { }
//                }
//            }
//        }
//    }
//    AnimatedVisibility(
//        visible = isShowingSelection,
//        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
//        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
//    ) {
//        ItemSelector(needsFilter = true, title = "foods", things = list) {
//            isShowingSelection = !isShowingSelection
//        }
//    }
//}
////if (rememberTop == 3) {
////    var rememberDrop by remember { mutableStateOf(false) }
////    Row(modifier = Modifier.fillMaxWidth()) {
////        Spacer(modifier = Modifier.weight(1F))
////        DimensionSubcomposeLayout(mainContent = {
////            val x = 0.dp
////            val y = 0.dp
////            Box {
////                IconButton(
////                    modifier = Modifier.width(IntrinsicSize.Min),
////                    onClick = { rememberDrop = !rememberDrop }) {
////                    Icon(
////                        painter = painterResource(id = R.drawable.round_filter_list_black_24),
////                        contentDescription = ""
////                    )
////                }
////                DropdownMenu(
////                    modifier = Modifier.background(MaterialTheme.colors.surface),
////                    expanded = rememberDrop,
////                    onDismissRequest = { rememberDrop = !rememberDrop }) {
////                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
////                        GraphDropDownTop(
////                            offset = DpOffset(x, y),
////                            title = "Chart",
////                            selectedItem = graphType,
////                            items = GraphType.values(),
////                            selectItem = { type ->
////                                rememberDrop = !rememberDrop
////                                graphType = type
////                            }
////                        )
////                    }
////                }
////            }
////        }) {
////            val width = with(LocalDensity.current) { it.width.toDp() }
////            val x = LocalConfiguration.current.screenWidthDp.dp - width
////            val y = 0.dp
////            Box {
////                IconButton(
////                    modifier = Modifier.width(IntrinsicSize.Min),
////                    onClick = { rememberDrop = !rememberDrop }) {
////                    Icon(
////                        painter = painterResource(id = R.drawable.round_filter_list_black_24),
////                        contentDescription = ""
////                    )
////                }
////                DropdownMenu(
////                    modifier = Modifier.background(MaterialTheme.colors.surface),
////                    expanded = rememberDrop,
////                    onDismissRequest = { rememberDrop = !rememberDrop }) {
////                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
////                        GraphDropDownTop(
////                            offset = DpOffset(x, y),
////                            title = "Chart",
////                            selectedItem = graphType,
////                            items = GraphType.values(),
////                            selectItem = { type ->
////                                rememberDrop = !rememberDrop
////                                graphType = type
////                            }
////                        )
////                    }
////                    GraphDropDownTopSplit(
////                        offset = DpOffset(x, y),
////                        title = "Grouping",
////                        selectedItem = rememberGroupType,
////                        items = CategoryType.values(),
////                        selectItem = { type ->
////                            rememberGroupType = type
////                            rememberDrop = !rememberDrop
////                        },
////                        selectedItem2 = rememberXAxisType,
////                        selectItem2 = { type ->
////                            rememberXAxisType = type
////                            rememberDrop = !rememberDrop
////                        },
////                        isSecondEnabled = graphType != GraphType.Bar
////                    )
////                    GraphDropDownTop(
////                        offset = DpOffset(x, y),
////                        title = "Measure",
////                        selectedItem = graphMeasurementType,
////                        items = GraphMeasurementType.values(),
////                        selectItem = { type ->
////                            graphMeasurementType = type
////                            rememberDrop = !rememberDrop
////                        }
////                    )
////                    GraphDropDownTop(
////                        offset = DpOffset(x, y),
////                        title = "Calculation",
////                        selectedItem = GraphCalculationType.Mean,
////                        items = GraphCalculationType.values(),
////                        selectItem = { rememberDrop = !rememberDrop }
////                    )
////                    GraphDropDownTop(
////                        offset = DpOffset(x, y),
////                        title = "Order",
////                        selectedItem = orderType,
////                        items = GraphOrderType.values(),
////                        selectItem = { type ->
////                            orderType = type
////                            rememberDrop = !rememberDrop
////                        }
////                    )
////                    Divider(modifier = Modifier.padding(vertical = 8.dp))
////                    GraphChartSelectionForm("Foods") {
////                        rememberDrop = !rememberDrop
////                        rememberSelectionItem = CategoryType.Food
////                    }
////                    GraphChartSelectionForm("Nutrients") {
////                        rememberDrop = !rememberDrop
////                        rememberSelectionItem = CategoryType.Nutrient
////                    }
////                    GraphChartSelectionForm("Partitions") {
////                        rememberDrop = !rememberDrop
////                        rememberSelectionItem = CategoryType.Partition
////                    }
////                    GraphChartSelectionForm("Time Span") {
////                        rememberDrop = !rememberDrop
////                        rememberSelectionItem = CategoryType.Time
////                    }
////                    GraphChartSelectionForm("Settings") {
////                        rememberDrop = !rememberDrop
////                    }
////                }
////            }
////        }
////    }
////}
////AnimatedVisibility(
////visible = rememberSelectionItem != CategoryType.None,
////enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
////exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
////) {
////    ItemSelector(
////        needsFilter = rememberSelectionItem == CategoryType.Food,
////        title = rememberSelectionItem.name,
////        things = list
////    ) {
////        rememberSelectionItem = CategoryType.None
////    }
////}
////}