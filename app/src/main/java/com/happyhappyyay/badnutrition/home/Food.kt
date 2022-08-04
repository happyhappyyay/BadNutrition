package com.happyhappyyay.badnutrition.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.data.food.Food
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

@Composable
fun Food() {
    LazyColumn{
        itemsIndexed(MockData().createFoodList()){ ind, item ->
            FoodItem(item)
        }
    }
    FloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(Icons.Filled.Add, contentDescription = "")
    }
}

@Composable
fun FoodItem(food : Food) {
    Card(backgroundColor = MaterialTheme.colors.background) {
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = food.name)
            Row{
                Text(text = "Kg")
                Icon(Icons.Filled.KeyboardArrowDown, "")
            }
        }
    }
}

@Preview
@Composable
fun PreviewFood() {
    BadNutritionTheme {
        Food()
    }
}

@Preview
@Composable
fun PreviewFoodItem() {
    BadNutritionTheme {
        FoodItem(Food(1L, "DEEEEEEZ NUTZZZ", ArrayList()))
    }
}