package com.happyhappyyay.badnutrition.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

@Composable
fun NutrientFields(names: Array<String>, values: MutableList<String> = MutableList(names.size){"0"}) {
    val nutrientAmounts = remember { values.toMutableStateList() }
    LazyColumn(modifier =
    Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        itemsIndexed(nutrientAmounts){ index, i ->
            if(index % 2 == 0) {
                Row{
                    NutrientField(
                        modifier = Modifier.weight(1f),
                        amount = i,
                        nutrientName = names[index],
                        onChange = {
                        nutrientAmounts[index] = it
                        }
                    )
                    if(index+1 < nutrientAmounts.size){
                        NutrientField(
                            modifier = Modifier.weight(1f),
                            amount = nutrientAmounts[index+1],
                            nutrientName = names[index+1],
                            onChange = {
                                nutrientAmounts[index+1] = it
                            }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun NutrientField(modifier: Modifier, amount: String, nutrientName:String, onChange:(String) -> Unit) {
    TextField(
        modifier = modifier.padding(bottom = 8.dp,start = 4.dp,end = 4.dp),
        value = amount,
        onValueChange = {
            onChange(it)
        },
        label = {
            Text(nutrientName)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

@Preview
@Composable
fun PreviewNutrientFields(){
    BadNutritionTheme{
        NutrientFields(names = arrayOf("dawg","water","creaks","ash","calcium","iron", "calcium", "manganese", "magnesium",
            "potassium", "sodium", "protein", "fat", "carbohydrates", "fiber",
            "sugar", "phosphorous", "iodine", "tocopherol", "calories"))
    }
}