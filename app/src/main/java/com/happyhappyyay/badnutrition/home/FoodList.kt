package com.happyhappyyay.badnutrition.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.R

@Composable
fun FoodList(modifier: Modifier = Modifier, names: Array<String>, updateNutrientValues: () -> Unit){
    LazyColumn(modifier =
    modifier
        .padding(8.dp, 0.dp)
        .fillMaxWidth()
        .fillMaxHeight()
    )
    {
        items(names){ name ->
            Row(modifier = modifier.clickable { updateNutrientValues()/** TODO: populate nutrients w/ saved food **/ }){
                Icon(modifier = Modifier.weight(.2f).padding(8.dp),
                    painter = painterResource(id = R.drawable.round_food_item_alt_black_24),
                    contentDescription = ""
                )
                FoodListItem(modifier = Modifier.weight(.8f),name = name)
            }
        }
    }
}

@Composable
fun FoodListItem(modifier: Modifier = Modifier, name: String){
    Text(
        modifier =
        modifier
            .padding(8.dp)
            .fillMaxWidth(),
        text = name
    )
}