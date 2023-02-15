package com.happyhappyyay.badnutrition.ui.graph

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.data.filter.NutritionFilter
import com.happyhappyyay.badnutrition.ui.home.nutrientItems
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.util.SimpleDialogHeading
import com.happyhappyyay.badnutrition.util.SimpleRadioButton
import com.happyhappyyay.badnutrition.util.SimpleSwitchButton

enum class SortTypes {
    ALPHABETICAL,
    DATE,
    FREQUENCY,
    NUTRIENT
}

@Composable
fun AddSort(nutrients: Array<String>, onAdd: (String) -> Unit, onDismiss: () -> Unit) {
    var buttonSelected by rememberSaveable { mutableStateOf(SortTypes.NUTRIENT) }
    var isNutrientExpanded by rememberSaveable { mutableStateOf(false) }
    var nutrientSelected by rememberSaveable { mutableStateOf("") }
    var isIncreasing by rememberSaveable { mutableStateOf(true) }
    var weight by rememberSaveable { mutableStateOf("100") }
    Card {
        val focusManager = LocalFocusManager.current
        Row {
            Column {
                SimpleDialogHeading(title = "Sort by")
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .border(
                                2.dp,
                                color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(vertical = 8.dp)
                    ) {

                        SimpleRadioButton(
                            label = "Alphabetical",
                            isEnabled = buttonSelected == SortTypes.ALPHABETICAL
                        ) { buttonSelected = SortTypes.ALPHABETICAL }
                        SimpleRadioButton(
                            label = "Date",
                            isEnabled = buttonSelected == SortTypes.DATE
                        ) { buttonSelected = SortTypes.DATE }
                        SimpleRadioButton(
                            label = "Frequency",
                            isEnabled = buttonSelected == SortTypes.FREQUENCY
                        ) { buttonSelected = SortTypes.FREQUENCY }
                        SimpleRadioButton(
                            label = "Nutrient",
                            isEnabled = buttonSelected == SortTypes.NUTRIENT
                        ) { buttonSelected = SortTypes.NUTRIENT }
                        if (buttonSelected == SortTypes.NUTRIENT) {
                            Row(modifier = Modifier.padding(8.dp)) {
                                SimpleDropDownMenu(
                                    modifier = Modifier.weight(1f),
                                    items = nutrients,
                                    isItemExpanded = isNutrientExpanded,
                                    label = "Nutrient",
                                    updateExpanded = { isNutrientExpanded = !isNutrientExpanded },
                                    updateSelected = { nutrient -> nutrientSelected = nutrient },
                                    itemSelected = nutrientSelected,
                                    isEnabled = true
                                )
                                OutlinedTextField(
                                    modifier = Modifier
                                        .weight(.5f)
                                        .padding(start = 8.dp, end = 8.dp, bottom = 6.dp),
                                    value = weight,
                                    onValueChange = { weight = it },
                                    label = {
                                        Text("Weight", color = MaterialTheme.colors.primary)
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
                            }

                        }
                    }
                    SimpleSwitchButton(
                        label1 = "Increasing", label2 = "Decreasing", isFirstPressed = isIncreasing
                    ) {
                        isIncreasing = !isIncreasing
                    }
                    SelectionConfirmation(onDismiss = { onDismiss() }) {
                        val sorting = if (buttonSelected != SortTypes.NUTRIENT) {
                            NutritionFilter(
                                itemName = buttonSelected.name,
                                comparison = if (isIncreasing) "increasing" else "decreasing"
                            )
                        } else {
                            NutritionFilter(
                                itemName = nutrientSelected,
                                comparison = if (isIncreasing) "increasing" else "decreasing",
                                amount = weight
                            )
                        }.toString()
                        onAdd(sorting)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddSort() {
    BadNutritionTheme {
        AddSort(nutrientItems, {}, {})
    }
}