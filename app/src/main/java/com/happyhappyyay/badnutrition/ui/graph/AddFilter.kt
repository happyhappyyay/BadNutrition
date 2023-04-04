package com.happyhappyyay.badnutrition.ui.graph

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.ui.calendar.Calendar
import com.happyhappyyay.badnutrition.data.filter.NutritionFilter
import com.happyhappyyay.badnutrition.ui.home.nutrientItems
import com.happyhappyyay.badnutrition.ui.home.unitItems
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.util.SimpleDialogHeading
import com.happyhappyyay.badnutrition.util.currentDayString

enum class FilterTypes(s: String) {
    NUTRIENT("Nutrient"),
    DATE("Entry Date"),
    FREQUENCY("Frequency");

    private val label = s
    fun getFilterTypeList(): Array<String> {
        return Array(values().size) {
            values()[it].label
        }
    }
}

val comparisonSigns = arrayListOf(
    "greater than",
    "less than",
    "greater than or equal to",
    "less than or equal to",
    "equal to",
    "not equal to"
).toTypedArray()

@Composable
fun AddFilter(
    nutrients: Array<String>,
    measurements: Array<String>,
    onAdd: (NutritionFilter) -> Unit,
    onDismiss: () -> Unit
) {
    var isFilterExpanded by rememberSaveable { mutableStateOf(false) }
    var filterSelected by rememberSaveable { mutableStateOf("Nutrient") }
    val focusManager = LocalFocusManager.current
    var date by rememberSaveable { mutableStateOf(currentDayString()) }
    var showCalendar by rememberSaveable { mutableStateOf(false) }

    AnimatedVisibility(visible = showCalendar) {
        Dialog(onDismissRequest = { showCalendar = false }) {
            Calendar(date = date, setDate = {
                date = it
                showCalendar = false
            })
        }
    }
    Card {
        Column {
            SimpleDialogHeading(title = "Filter by")
            val mod =
                if (LocalConfiguration.current.orientation != Configuration.ORIENTATION_PORTRAIT) Modifier.weight(
                    1f
                ) else Modifier
            LazyColumn(mod) {
                item {
                    Column {
                        SimpleDropDownMenu(
                            items = FilterTypes.NUTRIENT.getFilterTypeList(),
                            isItemExpanded = isFilterExpanded,
                            label = "Filter Category",
                            updateExpanded = { expanded -> isFilterExpanded = expanded },
                            updateSelected = { selected -> filterSelected = selected },
                            itemSelected = filterSelected,
                            isEnabled = true,
                            modifier = Modifier
                        )
                        DropDownCollection(
                            nutrients = nutrients,
                            focusManager = focusManager,
                            date = date,
                            type = when (filterSelected) {
                                "Entry Date" -> FilterTypes.DATE
                                "Frequency" -> FilterTypes.FREQUENCY
                                else -> FilterTypes.NUTRIENT
                            }
                        ) {
                            showCalendar = true
                        }


                    }
                }
            }
            SelectionConfirmation(onDismiss = onDismiss, onAdd = {
                if (filterSelected == FilterTypes.NUTRIENT.name) {
//                    NutritionFilter(itemName = )
                } else {
//                    NutritionFilter(itemName = )
                }
            })
        }
    }
}

@Composable
fun SelectionConfirmation(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onAdd: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { onDismiss() }) {
            Text("Cancel")
        }
        Button(onClick = {
            onAdd()
        }) {
            Text("Add")
        }
    }
}

@Composable
fun DropDownCollection(
    nutrients: Array<String>,
    focusManager: FocusManager,
    type: FilterTypes,
    date: String,
    onCalendarClick: () -> Unit,
) {
    var isDrop1Expanded by rememberSaveable { mutableStateOf(false) }
    var drop1Selected by rememberSaveable { mutableStateOf("") }
    var isDrop2Expanded by rememberSaveable { mutableStateOf(false) }
    var drop2Selected by rememberSaveable { mutableStateOf("") }
    var isDrop3Expanded by rememberSaveable { mutableStateOf(false) }
    var drop3Selected by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }

    val isNutrient = type == FilterTypes.NUTRIENT
    val isEntry = type == FilterTypes.DATE
    SimpleDropDownMenu(
        items = nutrients,
        isItemExpanded = isDrop1Expanded,
        label = "Nutrient",
        updateExpanded = { expanded -> isDrop1Expanded = expanded },
        updateSelected = { selected -> drop1Selected = selected },
        itemSelected = drop1Selected,
        isEnabled = isNutrient,
        modifier = Modifier
    )
    SimpleDropDownMenu(
        items = comparisonSigns,
        isItemExpanded = isDrop2Expanded,
        label = "Comparison",
        updateExpanded = { expanded -> isDrop2Expanded = expanded },
        updateSelected = { selected -> drop2Selected = selected },
        itemSelected = drop2Selected,
        isEnabled = true,
        modifier = Modifier
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 6.dp),
        value = if (isEntry) date else amount,
        onValueChange = {
            if (isEntry) {
            } else amount = it
        },
        label = {
            Text(if (isEntry) "Date" else "Amount", color = MaterialTheme.colors.primary)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        ),
        trailingIcon = {
            if (isEntry) {
                IconButton(onClick = { onCalendarClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_calendar_alt_black_32),
                        contentDescription = ""
                    )
                }
            }
        }
    )
    SimpleDropDownMenu(
        items = unitItems,
        isItemExpanded = isDrop3Expanded,
        label = "Unit",
        updateExpanded = { expanded -> isDrop3Expanded = expanded },
        updateSelected = { selected -> drop3Selected = selected },
        itemSelected = drop3Selected,
        isEnabled = type == FilterTypes.NUTRIENT,
        modifier = Modifier
    )
}

@Composable
fun SimpleDropDownMenu(
    items: Array<String>,
    isItemExpanded: Boolean,
    label: String,
    updateExpanded: (Boolean) -> Unit,
    updateSelected: (String) -> Unit,
    itemSelected: String,
    isEnabled: Boolean,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = itemSelected,
                onValueChange = {},
                label = {
                    Text(label, color = if (isEnabled) MaterialTheme.colors.primary else Color.Gray)
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.animateContentSize(),
                        imageVector = if (!isItemExpanded) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                        contentDescription = ""
                    )
                },
                readOnly = true,
                singleLine = true,
                enabled = isEnabled
            )
            val modifier =
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.fillMaxWidth(
                    .4f
                ) else Modifier.fillMaxWidth(.8f)
            DropdownMenu(modifier = modifier.padding(8.dp),
                expanded = isItemExpanded,
                onDismissRequest = { updateExpanded(false) }) {
                items.forEach { item ->
                    val selected = itemSelected == item
                    Column {
                        Text(text = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    updateSelected(item)
                                    updateExpanded(false)
                                }
                                .clip(MaterialTheme.shapes.small)
                                .background(
                                    color = if (selected) MaterialTheme.colors.primary
                                    else MaterialTheme.colors.background
                                )
                                .padding(4.dp),
                            color = if (selected) MaterialTheme.colors.onPrimary
                            else MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.subtitle1)
                    }
                }
            }
        }
        if (isEnabled) {
            Box(modifier = Modifier
                .padding(top = 8.dp)
                .clickable { updateExpanded(!isItemExpanded) }
                .fillMaxHeight()
                .fillMaxWidth()) {

            }
        }
    }
}

@Composable
fun FrequencyDropDownCollection(
    focusManager: FocusManager,
) {
    var isDrop1Expanded by rememberSaveable { mutableStateOf(false) }
    var drop1Selected by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }

    SimpleDropDownMenu(
        items = comparisonSigns,
        isItemExpanded = isDrop1Expanded,
        label = "Comparison",
        updateExpanded = { expanded -> isDrop1Expanded = expanded },
        updateSelected = { selected -> drop1Selected = selected },
        itemSelected = drop1Selected,
        isEnabled = true,
        modifier = Modifier
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 6.dp),
        value = amount,
        onValueChange = { amount = it },
        label = {
            Text("Amount", color = MaterialTheme.colors.primary)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        ),
    )
    Spacer(
        modifier = Modifier
            .height(70.dp)
            .padding(vertical = 8.dp)
    )
    Spacer(
        modifier = Modifier
            .height(70.dp)
            .padding(vertical = 8.dp)
    )
}

@Composable
fun EntryDropDownCollection(
    date: String,
    focusManager: FocusManager,
    showCalendar: () -> Unit
) {
    var isDrop1Expanded by rememberSaveable { mutableStateOf(false) }
    var drop1Selected by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf(date) }

    SimpleDropDownMenu(
        items = comparisonSigns,
        isItemExpanded = isDrop1Expanded,
        label = "Comparison",
        updateExpanded = { expanded -> isDrop1Expanded = expanded },
        updateSelected = { selected -> drop1Selected = selected },
        itemSelected = drop1Selected,
        isEnabled = true,
        modifier = Modifier
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 6.dp),
        value = date,
        onValueChange = { amount = it },
        label = {
            Text("Date", color = MaterialTheme.colors.primary)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        ),
        trailingIcon = {
            IconButton(onClick = { showCalendar() }) {
                Icon(
                    painter = painterResource(id = R.drawable.round_calendar_alt_black_32),
                    contentDescription = ""
                )
            }
        }
    )
    Spacer(
        modifier = Modifier
            .height(70.dp)
            .padding(vertical = 8.dp)
    )
    Spacer(
        modifier = Modifier
            .height(70.dp)
            .padding(vertical = 8.dp)
    )
}

@Preview
@Composable
fun AddFilterItemPreview() {
    BadNutritionTheme {
        AddFilter(nutrientItems, unitItems, {}, {})
    }
}

@Preview
@Composable
fun FilterDropDownMenuPreview() {
    BadNutritionTheme {
        SimpleDropDownMenu(nutrientItems, true, "label", {}, {}, nutrientItems[0], false, Modifier)
    }
}