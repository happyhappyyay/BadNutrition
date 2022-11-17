package com.happyhappyyay.badnutrition.graph

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.data.filter.NutritionFilter
import com.happyhappyyay.badnutrition.home.unitItems
import com.happyhappyyay.badnutrition.home.nutrientItems
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

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
    var isNutrientExpanded by rememberSaveable { mutableStateOf(false) }
    var nutrientSelected by rememberSaveable { mutableStateOf("") }
    var isComparisonExpanded by rememberSaveable { mutableStateOf(false) }
    var comparisonSelected by rememberSaveable { mutableStateOf("") }
    var isUnitExpanded by rememberSaveable { mutableStateOf(false) }
    var unitSelected by rememberSaveable { mutableStateOf("") }
    var nutrientAmount by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Card {
        Column {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.primary)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Add A Filter",
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
            Column {
                FilterDropDownMenu(label = "Nutrient",
                    items = nutrients,
                    isItemExpanded = isNutrientExpanded,
                    itemSelected = nutrientSelected,
                    updateExpanded = { expanded -> isNutrientExpanded = expanded },
                    updateSelected = { selected -> nutrientSelected = selected })
                FilterDropDownMenu(
                    items = comparisonSigns,
                    isItemExpanded = isComparisonExpanded,
                    label = "Comparison",
                    updateExpanded = { expanded -> isComparisonExpanded = expanded },
                    updateSelected = { selected -> comparisonSelected = selected },
                    itemSelected = comparisonSelected
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    value = nutrientAmount,
                    onValueChange = { nutrientAmount = it },
                    label = {
                        Text("Amount", color = MaterialTheme.colors.primary)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() },
                    ),
                )
                FilterDropDownMenu(
                    items = unitItems,
                    isItemExpanded = isUnitExpanded,
                    label = "Unit",
                    updateExpanded = { expanded -> isUnitExpanded = expanded },
                    updateSelected = { selected -> unitSelected = selected },
                    itemSelected = unitSelected
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        onAdd(
                            NutritionFilter(
                                itemName = nutrientSelected,
                                comparison = comparisonSelected,
                                amount = nutrientAmount,
                                unit = unitSelected
                            )
                        )
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDropDownMenu(
    items: Array<String>,
    isItemExpanded: Boolean,
    label: String,
    updateExpanded: (Boolean) -> Unit,
    updateSelected: (String) -> Unit,
    itemSelected: String
) {
    Box(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column {
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                value = itemSelected,
                onValueChange = {},
                label = {
                    Text(label, color = MaterialTheme.colors.primary)
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.animateContentSize(),
                        imageVector = if (!isItemExpanded) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                        contentDescription = ""
                    )
                },
                readOnly = true,
                singleLine = true
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
                                .background(
                                    color = if (selected) MaterialTheme.colors.primary
                                    else MaterialTheme.colors.background
                                )
                                .fillMaxWidth(),
                            color = if (selected) MaterialTheme.colors.onPrimary
                            else MaterialTheme.colors.onBackground)
                    }
                }
            }
        }
        Box(modifier = Modifier
            .padding(top = 8.dp)
            .clickable { updateExpanded(!isItemExpanded) }
            .fillMaxHeight()
            .fillMaxWidth()) {

        }
    }
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
        FilterDropDownMenu(nutrientItems, true, "label", {}, {}, nutrientItems[0])
    }
}