package com.happyhappyyay.badnutrition.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.data.food.Food
import com.happyhappyyay.badnutrition.data.nutrient.Nutrient
import com.happyhappyyay.badnutrition.data.nutrient.calcNutrientValPercent
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.Shapes
import com.happyhappyyay.badnutrition.ui.theme.halfRoundedShape

@Composable
fun FoodScreen(date: String, setType: (HomeType) -> Unit, dismiss: () -> Unit){
    val strings = arrayOf("Breakfast", "Lunch", "Dinner")
    LazyColumn(modifier = Modifier.padding(start = 8.dp,end = 8.dp)){
        item { ScreenHeading(type = HomeType.Food, setType,dismiss) }
        itemsIndexed(strings) { i,_ ->
            FoodPartition(strings[i])
        }
    }
}

@Composable
fun FoodPartition(title: String = "Breakfast"){
    var isExpanded by remember { mutableStateOf(true)}
    Column(modifier = Modifier.padding(0.dp, 8.dp)){
        Card (
            modifier = Modifier
                .padding(0.dp,0.dp,0.dp,0.dp),
            elevation = 8.dp,
            shape = if(isExpanded) halfRoundedShape else Shapes.medium
        ){
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Icon(
                        if (isExpanded) Icons.Rounded.KeyboardArrowUp
                        else Icons.Rounded.KeyboardArrowDown,
                        contentDescription =""
                    )
                }
            }
        }
        AnimatedVisibility(visible = isExpanded) {
            Foods()
        }
    }
}

@Composable
fun Foods() {
    Column (modifier = Modifier.fillMaxHeight()
            ,horizontalAlignment = Alignment.End
    ){
        MockData().createFoodList().forEach{
            FoodItem(it)
        }
//        FloatingActionButton(onClick = { /*TODO*/ }) {
//            Icon(Icons.Filled.Add, contentDescription = "")
//        }
    }
}

@Composable
fun FoodItem(food : Food) {
    var isExpanded by remember { mutableStateOf(false)}
    Card(modifier = Modifier
        .padding(16.dp,2.dp,0.dp,0.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
        shape = MaterialTheme.shapes.large,
    ) {
        IconButton(onClick = { isExpanded = !isExpanded }) {
            Column(modifier = Modifier
                .padding(8.dp, 2.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = food.name)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Kg")
                        Icon(
                            if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            ""
                        )
                    }
                }
                AnimatedVisibility(visible = isExpanded) {
                    NutrientItems()
                }
            }
        }
    }
}
@Composable
fun NutrientItems(nutrients: List<Nutrient> = MockData().createNutritionList()){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier
            .fillMaxWidth(.5F)
            .padding(8.dp, 0.dp)) {
            for(i in nutrients.indices step 2) {
                NutrientItem(nutrients[i])
            }

        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 0.dp),
            horizontalAlignment = Alignment.Start) {
            for(i in 1 until nutrients.size step 2) {
                NutrientItem(nutrients[i])
            }
        }
    }
}
@Composable
fun NutrientItem(nutrient: Nutrient){
    val percentage = calcNutrientValPercent(nutrient.value, nutrient.goal.min)
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = nutrient.name)
            Text(
                text = "${calcNutrientValPercent(nutrient.value, nutrient.goal.min)}%",
                textAlign = TextAlign.End
            )
        }
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(text = when {
//                nutrient.goal.max < 0 -> "${nutrient.goal.min}${nutrient.measurement} min"
//                nutrient.goal.min <= 0 -> "${nutrient.goal.max}${nutrient.measurement} max"
//                else -> "${nutrient.goal.min}${nutrient.measurement} - ${nutrient.goal.max}${nutrient.measurement}"
//            })
//            Text(text = "${nutrient.goal.min}${nutrient.measurement}",
//                textAlign = TextAlign.End)
//        }
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth(.9F)
//                    .height(6.dp)
//                    .clip(CircleShape)
//                    .background(emptyBarColor)
//                    .border(BorderStroke(1.dp, Color.Gray), shape = CircleShape)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .padding(0.dp, 1.dp)
//                        .fillMaxWidth((percentage/100F) * .994F)
//                        .height(4.dp)
//                        .clip(CircleShape)
//                        .background(if (percentage in 51..100) adequateRangeColor else deficientRangeColor)
//                )
//            }
    }
}

@Preview
@Composable
fun PreviewFood() {
    BadNutritionTheme {
        Foods()
    }
}

@Preview
@Composable
fun PreviewFoodItem() {
    BadNutritionTheme {
        FoodItem(Food(1L, "DEEEEEEZ NUTZZZ", ArrayList()))
    }
}

@Preview
@Composable
fun PreviewFoodScreen() {
    BadNutritionTheme {
        FoodScreen("12991012", {}) {}
    }
}