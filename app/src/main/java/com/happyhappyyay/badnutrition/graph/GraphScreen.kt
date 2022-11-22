package com.happyhappyyay.badnutrition.graph

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.charts.Chart
import com.happyhappyyay.badnutrition.charts.ChartType
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.halfRoundedShapeLeft
import com.happyhappyyay.badnutrition.ui.theme.halfRoundedShapeTop
import com.happyhappyyay.badnutrition.util.SimpleCheckBox
import com.happyhappyyay.badnutrition.util.SimpleRadioButton

enum class CategoryType {
    Food, Nutrient, Partition, Time
}

enum class GraphMeasurementType {
    Amount, Entries, Exists, Percentage, Servings
}

enum class GraphCalculationType {
    Max, Mean, Median, Min, Mode, Sum
}

@Composable
fun GraphScreen() {
    var isFullScreen by rememberSaveable { mutableStateOf(false) }
    var chartType by rememberSaveable { mutableStateOf(ChartType.Bar) }
    var categoryType by rememberSaveable { mutableStateOf(CategoryType.Nutrient) }
    var graphMeasurementType by rememberSaveable { mutableStateOf(GraphMeasurementType.Percentage) }
    var includeEmptyEntries by rememberSaveable { mutableStateOf(false) }
    var includeValueRange by rememberSaveable { mutableStateOf(false) }
    var minimumValue by rememberSaveable { mutableStateOf(0) }
    var maximumValue by rememberSaveable { mutableStateOf(0) }
    var isShowingSelection by rememberSaveable { mutableStateOf(true) }

    OrientationBasedGraphFrame(composable1 = { modifier ->
        ChosenChart(
            modifier = modifier.animateContentSize(),
            type = chartType,
            data = MockData().nutrientItems,
            heading = "Summary of The Greatest Nutrients Ever!"
        )
    }, composable2 = {
        ControlBox(
            selectedChart = chartType,
            onSelectChart = { selected ->
                chartType = selected
            },
            expandControl = {
                isFullScreen = true
            },
            onSelectCategory = { selected ->
                categoryType = selected
            },
            selectedCategory = categoryType,
            onSelectMetric = { selected ->
                graphMeasurementType = selected
            },
            selectedMetric = graphMeasurementType,
            onSelectEntries = {
                includeEmptyEntries = !includeEmptyEntries
            },
            selectedEntries = includeEmptyEntries
        ) {
            isShowingSelection = !isShowingSelection
        }
    }, isFullScreen
    )
    AnimatedVisibility(visible = isFullScreen, enter = fadeIn(), exit = fadeOut()) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            IconButton(onClick = { isFullScreen = false }) {
                Icon(
                    modifier = Modifier.animateContentSize(),
                    painter = painterResource(id = R.drawable.round_fullscreen_exit_alt_black_48),
                    contentDescription = ""
                )
            }
        }
    }
    AnimatedVisibility(
        visible = isShowingSelection,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
    ) {
        ItemSelector(things = list, title = "foods", needsFilter = true) {
            isShowingSelection = !isShowingSelection
        }
    }
}

@Composable
fun ChosenChart(
    modifier: Modifier = Modifier, type: ChartType, data: Array<Float>, heading: String = "Summary"
) {
    when (type) {
        ChartType.Bar -> {
            Chart(modifier, ChartType.Bar, data, heading)
        }
        ChartType.Line -> {
            Chart(modifier, ChartType.Line, data, heading)
        }
        ChartType.Scatter -> {

        }
    }
}

@Composable
fun OrientationBasedGraphFrame(
    composable1: @Composable (modifier: Modifier) -> Unit,
    composable2: @Composable (modifier: Modifier) -> Unit,
    isFullScreen: Boolean
) {
    val orientation = LocalConfiguration.current.orientation
    if (orientation == Configuration.ORIENTATION_PORTRAIT || orientation == Configuration.ORIENTATION_UNDEFINED) {
        val modifier = Modifier.fillMaxWidth()
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
            composable1(
                if (isFullScreen) modifier.fillMaxHeight()
                else modifier.fillMaxHeight(.7f)
            )
            AnimatedVisibility(visible = !isFullScreen) {
                composable2(Modifier.fillMaxHeight(.2f))
            }
        }
    } else {
        val modifier = Modifier.fillMaxHeight()
        Row {
            composable1(
                if (isFullScreen) modifier.fillMaxWidth()
                else modifier.fillMaxWidth(.7f)
            )
            AnimatedVisibility(visible = !isFullScreen) {
                composable2(Modifier.fillMaxWidth(.3f))
            }
        }
    }
}

@Composable
fun OrientationBasedGraphControl(composable: @Composable () -> Unit) {
    val orientation = LocalConfiguration.current.orientation
    if (orientation == Configuration.ORIENTATION_PORTRAIT || orientation == Configuration.ORIENTATION_UNDEFINED) {
        LazyRow(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                composable()
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            item {
                composable()
            }
        }
    }
}

@Composable
fun ControlBox(
    selectedChart: ChartType,
    onSelectChart: (ChartType) -> Unit,
    expandControl: () -> Unit,
    onSelectCategory: (CategoryType) -> Unit,
    selectedCategory: CategoryType,
    onSelectMetric: (GraphMeasurementType) -> Unit,
    selectedMetric: GraphMeasurementType,
    onSelectEntries: () -> Unit,
    selectedEntries: Boolean,
    graphChartSelection: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 8.dp),
        elevation = 0.dp,
        shape = halfRoundedShapeLeft
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary)
                    .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Graph Circuit Board", modifier = Modifier.weight(.9f))
                IconButton(modifier = Modifier.weight(.1f), onClick = { expandControl() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_fullscreen_alt_black_40),
                        contentDescription = ""
                    )
                }
            }

            OrientationBasedGraphControl {
                GraphRadioForm(
                    title = "Chart Type",
                    selectedItem = selectedChart,
                    items = ChartType.values(),
                    selectItem = onSelectChart
                )
                GraphRadioForm(
                    title = "X Axis",
                    selectedItem = selectedCategory,
                    items = CategoryType.values(),
                    selectItem = onSelectCategory
                )
                GraphRadioForm(
                    title = "Y Axis",
                    selectedItem = selectedMetric,
                    items = GraphMeasurementType.values(),
                    selectItem = onSelectMetric
                )
                GraphRadioForm(title = "Calculations",
                    selectedItem = GraphCalculationType.Mean,
                    items = GraphCalculationType.values(),
                    selectItem = { })
                GraphChartSelectionForm("Foods") { graphChartSelection() }
                GraphChartSelectionForm("Nutrients") { graphChartSelection() }
                GraphChartSelectionForm("Partitions") { graphChartSelection() }
                GraphChartSelectionForm("Time Span") { graphChartSelection() }
                EntryForm(selectedEntries, onSelectEntries)
            }
        }
    }
}

@Composable
fun <T : Enum<T>> GraphRadioForm(
    selectedItem: T,
    items: Array<T>,
    selectItem: (T) -> Unit,
    title: String,
) {
    GraphChartForm(title) {
        items.forEach { type ->
            SimpleRadioButton(label = type.name, isEnabled = selectedItem == type, onClick = {
                selectItem(type)
            })
        }
    }
}

@Composable
fun GraphChartSelectionForm(title: String, filterItems: (String) -> Unit) {
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .width(200.dp)
            .fillMaxHeight(), elevation = 2.dp
    ) {
        Column(Modifier.fillMaxHeight()) {
            Text(
                "$title:",
                modifier = Modifier
                    .clip(halfRoundedShapeTop)
                    .background(MaterialTheme.colors.primary)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "All selected", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                )
                TextButton(modifier = Modifier.fillMaxWidth(), onClick = { filterItems(title) }) {
                    Text("Select $title")
                }
            }
        }
    }
}

@Composable
fun GraphChartForm(title: String, content: @Composable () -> Unit) {
    val orientation = LocalConfiguration.current.orientation
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .width(200.dp)
            .fillMaxHeight(), elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "$title:",
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary
            )
            if (orientation == Configuration.ORIENTATION_PORTRAIT || orientation == Configuration.ORIENTATION_UNDEFINED) {
                LazyColumn(Modifier.fillMaxHeight()) {
                    item {
                        content()
                    }
                }
            } else {
                content()
            }
        }
    }
}

@Composable
fun EntryForm(selectedEntries: Boolean, onSelectEntries: () -> Unit) {
    GraphChartForm(title = "Add") {
        SimpleCheckBox(label = "Empty Entries", isChecked = selectedEntries) {
            onSelectEntries()
        }
        SimpleCheckBox(label = "Value Range", isChecked = false) {

        }
        OutlinedTextField(
            modifier = Modifier.padding(8.dp),
            value = "0",
            onValueChange = {},
            label = { Text("Minimum") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            modifier = Modifier.padding(8.dp),
            value = "100",
            onValueChange = {},
            label = { Text("Maximum") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
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
            ChartType.Bar,
            {},
            {},
            {},
            CategoryType.Food,
            {},
            GraphMeasurementType.Exists,
            { },
            true,
            {}
        )
    }
}

@Preview
@Composable
fun PreviewChartTypeRadioForm() {
    BadNutritionTheme {
        GraphRadioForm(ChartType.Bar, ChartType.values(), {}, "Chart Type")
    }
}