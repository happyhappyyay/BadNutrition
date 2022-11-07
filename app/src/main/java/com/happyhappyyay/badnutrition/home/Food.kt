package com.happyhappyyay.badnutrition.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.data.food.Food
import com.happyhappyyay.badnutrition.data.nutrient.Nutrient
import com.happyhappyyay.badnutrition.data.nutrient.calcNutrientValPercent
import com.happyhappyyay.badnutrition.ui.theme.*
import com.happyhappyyay.badnutrition.util.FloatingAddButton
import com.happyhappyyay.badnutrition.util.ScrollButton
import kotlinx.coroutines.launch

@Composable
fun FoodScreen(date: String, setType: (HomeType) -> Unit, dismiss: () -> Unit){
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var dismissAddFood by rememberSaveable { mutableStateOf(true) }
    val showButton by remember{
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    AnimatedVisibility(visible = !dismissAddFood) {
        Dialog(onDismissRequest = { dismissAddFood = true }) {
            AddFood(
                onAddFood =  { food ->
                dismissAddFood = true
            },
                onCancel = {
                    dismissAddFood = true
                }
            )
        }
    }
    val strings = arrayOf("Breakfast", "Lunch", "Dinner")
    LazyColumn(
        state = listState,
        modifier = Modifier.padding(start = 8.dp,end = 8.dp)
    )
    {
        item { ScreenHeading(type = HomeType.Food, setType,dismiss) }
        itemsIndexed(strings) { i,_ ->
            FoodPartition(strings[i])
        }
        item{
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(64.dp))
        }
    }
    AnimatedVisibility(visible = showButton
    ) {
        ScrollButton {
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }
    FloatingAddButton {
        dismissAddFood = false
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
            backgroundColor = MaterialTheme.colors.primaryVariant,
            shape = if(isExpanded) halfRoundedShapeLeft else Shapes.medium
        ){
            Button(colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary),
                onClick = { isExpanded = !isExpanded }) {
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
        MockData().createFoodList().forEachIndexed(){ i, food ->
            FoodItem(food, GraphBarColors[i],i % 2 == 0)
        }

    }
}

@Composable
fun FoodItem(food: Food, color: Color, left: Boolean) {
    var isExpanded by remember { mutableStateOf(false)}
    Card(modifier = Modifier
        .padding(16.dp,3.dp,0.dp,0.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
        shape = MaterialTheme.shapes.large,
    ) {
            Column(
                Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(start = 8.dp)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = Modifier.weight(.55f), text = food.name)
                    Surface(
                        modifier = Modifier.weight(.45F),
                        color = color,
                        shape = if(left)leftBottomSpikeShape else leftTopSpikeShape,
                        elevation = 0.dp
                    ) {
                    Row(
                        Modifier.padding(0.dp,8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                            Text(modifier = Modifier, text = "Kg")
                            Icon(
                                if (isExpanded) Icons.Rounded.KeyboardArrowUp
                                else Icons.Rounded.KeyboardArrowDown,
                                ""
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = isExpanded) {
                    NutrientItems()
                }
        }
    }
}
@Composable
fun NutrientItems(nutrients: List<Nutrient> = MockData().createNutritionList()){
    Column{
        Surface(modifier = Modifier
            .height(2.dp)
            .padding(end = 8.dp)
            .fillMaxWidth(),
            color = MaterialTheme.colors.secondary){}
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
fun PreviewFoodScreen() {
    BadNutritionTheme {
        FoodScreen("12991012", {}) {}
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
        FoodItem(Food(1L, "DEEEEEEZ NUTZZZ", ArrayList()), Color.Cyan, true)
    }
}
