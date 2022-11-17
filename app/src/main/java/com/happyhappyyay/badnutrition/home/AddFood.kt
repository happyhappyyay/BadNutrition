package com.happyhappyyay.badnutrition.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.data.food.Food
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.halfRoundedShapeTop
import com.happyhappyyay.badnutrition.ui.theme.roundedShapeTop
import com.happyhappyyay.badnutrition.ui.theme.smallRoundShape

enum class AddFoodType {
    Barcode,
    Manual
}

val nutrientItems = arrayOf(
    "dawg", "water", "creaks", "ash", "calcium", "iron", "calcium", "manganese", "magnesium",
    "potassium", "sodium", "protein", "fat", "carbohydrates", "fiber",
    "sugar", "phosphorous", "iodine", "tocopherol", "calories"
)
val unitItems = arrayOf("kg", "lb", "mi", "g", "mg", "ug")

@Composable
fun AddFood(onCancel: () -> Unit, onAddFood: (Food) -> Unit) {
    var name by rememberSaveable { mutableStateOf("Kimchi") }
    var amount by rememberSaveable { mutableStateOf("1000") }
    var measure by rememberSaveable { mutableStateOf("Kg") }
    var isSaving by rememberSaveable { mutableStateOf(true) }
    var isSearching by rememberSaveable { mutableStateOf(true) }
    var selected by rememberSaveable { mutableStateOf(AddFoodType.Manual) }
    var isMeasureDropped by rememberSaveable { mutableStateOf(false) }
    var isSearchDropped by rememberSaveable { mutableStateOf(false) }
    var dropSelected by rememberSaveable { mutableStateOf("Kg") }
    val onPrimaryCol = TextFieldDefaults.textFieldColors().leadingIconColor(
        enabled = true,
        isError = false
    ).value
    val focusCol = TextFieldDefaults.textFieldColors().cursorColor(isError = false).value
    val textCol = TextFieldDefaults.textFieldColors().placeholderColor(enabled = true).value
    val buttonCol = TextFieldDefaults.textFieldColors().backgroundColor(enabled = true).value
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Card(
        Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background,
        shape = RectangleShape
    )
    {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            if (!isSearchDropped) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.primary
                ) {
                    Text(
                        "Add Food",
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center
                    )
                    Box(contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = { onCancel() }) {
                            Icon(Icons.Filled.Clear, contentDescription = "")
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                )
                {
                    Button(
                        enabled = selected != AddFoodType.Barcode,
                        onClick = {
                            selected = AddFoodType.Barcode
                            focusManager.clearFocus()
                        }
                    ) {
                        Text(AddFoodType.Barcode.toString())
                    }

                    Button(
                        enabled = selected != AddFoodType.Manual,
                        onClick = {
                            selected = AddFoodType.Manual
                            focusManager.clearFocus()
                        }
                    ) {
                        Text(AddFoodType.Manual.toString())
                    }
                }
            }

            Box(Modifier.animateContentSize())
            {
                Column {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 0.dp)
                            .focusRequester(focusRequester),
                        value = name,
                        onValueChange = {
                            name = it
                            if (selected == AddFoodType.Manual && isSearching) {
                                isSearchDropped = true
                            }
                        },
                        label = { Text("Name") },
                        singleLine = true,
                    )

                    if (isSearchDropped) {
                        FoodList(names = nutrientItems) {

                        }
                    }
                }
                if (selected == AddFoodType.Manual) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Row(modifier =
                        Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                isSearching = !isSearching
                                isSearchDropped = false
                            }
                        )
                        {
                            if (!isSearchDropped) {
                                Text(
                                    "Search",
                                    modifier = Modifier.padding(top = 8.dp),
                                    style = MaterialTheme.typography.caption
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                        .clip(smallRoundShape)
                                        .border(
                                            1.5.dp,
                                            if (isSearching) Color.Transparent else MaterialTheme.colors.onBackground,
                                            smallRoundShape
                                        )
                                        .background(if (isSearching) MaterialTheme.colors.secondary else Color.Transparent)
                                ) {
                                    if (isSearching) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "",
                                            tint = MaterialTheme.colors.onPrimary
                                        )
                                    }

                                }
                            } else {
                                IconButton(onClick = { isSearchDropped = false }) {
                                    Icon(Icons.Filled.Clear, contentDescription = "")
                                }
                            }
                        }
                    }
                }

            }
            if (!isSearchDropped) {
                Row(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp))
                {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(.75f)
                            .padding(end = 8.dp)
                            .focusRequester(focusRequester),
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    Column(Modifier.padding(bottom = 0.dp)) {
                        Surface(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    focusManager.clearFocus()
                                    isMeasureDropped = !isMeasureDropped
                                },
                            color = buttonCol,
                            shape = roundedShapeTop,

                            ) {
                            Column(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 8.dp,
                                    bottom = 9.dp
                                )
                            )
                            {
                                Text(
                                    "Measure",
                                    style = MaterialTheme.typography.caption,
                                    color = if (isMeasureDropped) focusCol else textCol
                                )

                                Text(text = dropSelected)
                                DropdownMenu(
                                    modifier = Modifier.fillMaxWidth(.25f),
                                    expanded = isMeasureDropped,
                                    onDismissRequest = { isMeasureDropped = !isMeasureDropped }
                                ) {
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(start = 8.dp)
                                    ) {
                                        unitItems.forEach {
                                            Text(
                                                text = it,
                                                modifier = Modifier
                                                    .clickable {
                                                        dropSelected = it
                                                        isMeasureDropped = !isMeasureDropped
                                                    }
                                                    .background(
                                                        color = if (dropSelected == it) focusCol
                                                        else MaterialTheme.colors.background
                                                    )
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Divider(
                            Modifier
                                .fillMaxWidth(),
                            thickness = 1.dp,
                            color = if (isMeasureDropped) focusCol else onPrimaryCol
                        )
                    }
                }
                Column(
                    Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    when (selected) {
                        AddFoodType.Manual -> {
                            Column(modifier = Modifier.weight(.9f)) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 4.dp, end = 4.dp, bottom = 0.dp),
                                    color = MaterialTheme.colors.primary,
                                    elevation = 4.dp,
                                    shape = halfRoundedShapeTop
                                )
                                {
                                    Text(
                                        text = stringResource(R.string.add_food_nutrient_input),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                NutrientFields(names = nutrientItems)
                            }
                        }
                        AddFoodType.Barcode -> {
                            Barcode()
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .background(MaterialTheme.colors.primary),
                        Alignment.BottomCenter,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                Modifier
                                    .weight(1f)
                                    .clickable { isSaving = !isSaving },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Checkbox(checked = isSaving, onCheckedChange = {})
                                Text(
                                    "Save Food",
                                    modifier = Modifier,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.onPrimary
                                )
                            }
                            Text(
                                "Add",
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        onAddFood(MockData().food)
                                    },
                                style = MaterialTheme.typography.h6,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddFoodPreview() {
    BadNutritionTheme {
        AddFood(onCancel = {}) {}
    }
}