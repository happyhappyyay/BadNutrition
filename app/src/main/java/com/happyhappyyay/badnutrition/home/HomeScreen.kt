package com.happyhappyyay.badnutrition.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.data.nutrient.Nutrient
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme


enum class HomeScreen {
    Day, Week, Month, Year
}

enum class HomeStyle {
    Simple, Standard, Complex
}

@Composable
fun Home(viewModel: HomeViewModel){
    Scaffold() {
        HomeContent()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent() {
    val style by remember { mutableStateOf(HomeStyle.Standard)}
    BackdropScaffold(
        modifier = Modifier,
        appBar = { NutritionAppBar() },
        backLayerContent = { HomeBackLayer() },
        frontLayerContent = { HomeFrontLayer(style = style) },
        gesturesEnabled = true
    ) {
    }
}

@Composable
fun HomeBackLayer() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.primaryVariant
    ) {

    }
}

@Composable
fun HomeFrontLayer(style: HomeStyle) {
    Column {
        NutritionHeading()
        LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp)){
            items(MockData().createNutritionList()) { nutritionItem ->
                when(style) {
                    HomeStyle.Simple -> SimpleNutritionCard(nutritionItem)
                    HomeStyle.Standard -> StandardNutritionCard(nutritionItem)
                    HomeStyle.Complex -> ComplexNutritionCard(nutritionItem)
                }
            }
        }
    }
}

@Composable
fun SimpleNutritionCard(nutrient: Nutrient) {
    val percentage = Math.round((nutrient.value/(nutrient.goal.min * 1F))*100)
    val zero: String = if(percentage < 10)"0" else ""
    val label = nutrient.name
    val maxWidth = if(percentage > 100) 1F else percentage/100F
    val overMax = nutrient.value > nutrient.goal.max

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(60.dp),
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Text(label)
            Row (Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8F)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .align(Alignment.CenterVertically)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(maxWidth * 1F)
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(if (!overMax && percentage > 50) Color.Green else Color.Red)
                    )
                }
                Text("$zero$percentage%", Modifier.padding(end = 8.dp))
            }
        }
    }
}

@Composable
fun ComplexNutritionCard(nutrient: Nutrient){
    val percentage = Math.round((nutrient.value/(nutrient.goal.min * 1F))*100)
    val zero: String = if(percentage < 10)"0" else ""
    val label = nutrient.name
    val maxWidth = if(percentage > 100) 1F else percentage/100F
    val overMax = nutrient.value > nutrient.goal.max

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(60.dp),
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Text(label)
            Row (Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Box(
                    modifier = Modifier
                        .fillMaxWidth(maxWidth * 0.8F)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(if (!overMax && percentage > 50) Color.Green else Color.Red)
                        .align(Alignment.CenterVertically)
                )
                Text("$zero$percentage%", Modifier.padding(end = 8.dp))
            }
        }
    }
}

@Composable
fun StandardNutritionCard(nutrient: Nutrient){
    val percentage = if(nutrient.goal.min > 0)
            Math.round((nutrient.value/(nutrient.goal.min * 1F))*100)
        else
            if(nutrient.value <= nutrient.goal.max)
                100
            else
                Math.round((nutrient.value/(nutrient.goal.max * 1F))*100)
    val zero: String = if(percentage < 10)"0" else ""
    val label = nutrient.name
    val maxWidth = if(percentage > 100) 1F else percentage/100F
    val overMax = if(nutrient.goal.max > 0) nutrient.value > nutrient.goal.max else false

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight(),
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Row (Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = label,
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "$zero$percentage%",
                    modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                    )
            }
            Column (modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = when {
                            nutrient.goal.max < 0 -> "Minimum: ${nutrient.goal.min}${nutrient.measurement}"
                            nutrient.goal.min <= 0 -> "Maximum: ${nutrient.goal.max}${nutrient.measurement}"
                            else -> "${nutrient.goal.min}${nutrient.measurement} - ${nutrient.goal.max}${nutrient.measurement}"
                        }

                    )
                    Text(
                        text = "${nutrient.value}${nutrient.measurement}"
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.90F)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(maxWidth * 1F)
                            .height(12.dp)
                            .clip(CircleShape)
                            .background(if (!overMax && percentage > 50) Color.Green else Color.Red)
                    )
                }
            }
        }
    }
}

@Composable
fun NutritionHeading() {
    Surface(
        modifier = Modifier
            .height(45.dp)
            .fillMaxWidth(),
        elevation = 8.dp,

    ) {
//        Box(Modifier.background(Brush.horizontalGradient(
//            listOf(MaterialTheme.colors.background,MaterialTheme.colors.primaryVariant),
//            0f,5000f
//        )))
        Text(
            modifier = Modifier
                .padding(bottom = 0.dp)
                .height(40.dp),
            text = "Nutrition",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )
    }

}

@Composable
fun NutritionAppBar(){
    var selected by remember { mutableStateOf(HomeScreen.Day)}
    var filter by remember {
        mutableStateOf(false)
    }
    TopAppBar(backgroundColor = MaterialTheme.colors.primaryVariant,) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        )
        {
            IconButton(onClick = { }) {
                Icon(Icons.Rounded.ArrowBack,null)
            }
            NutritionFilterItem(screen = HomeScreen.Day, selected = selected, onClick = {selected = it} )
            NutritionFilterItem(screen = HomeScreen.Week, selected = selected, onClick = {selected = it})
            NutritionFilterItem(screen = HomeScreen.Month,selected = selected, onClick = {selected = it})
            NutritionFilterItem(screen = HomeScreen.Year, selected = selected, onClick = {selected = it})
            IconButton(onClick = { filter = !filter }) {
                Icon(painter = painterResource(
                    id = if(filter)
                            R.drawable.round_filter_alt_off_black_24 else
                            R.drawable.round_filter_alt_black_24),
                    contentDescription = null)
            }
        }
    }
}

@Composable
fun NutritionFilterItem(screen: HomeScreen, selected: HomeScreen, modifier: Modifier = Modifier, onClick: (HomeScreen)-> Unit) {
    val color by animateColorAsState(
        if(screen == selected)
            MaterialTheme.colors.primaryVariant
            else MaterialTheme.colors.primary,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 20,
            easing = FastOutSlowInEasing
        ))
    val fontWeight = if(screen == selected) FontWeight.Bold else FontWeight.Normal
    Box(
        modifier = Modifier.clickable(
            indication = rememberRipple(color = MaterialTheme.colors.primaryVariant),
            interactionSource = remember { MutableInteractionSource() }
        )
        {
            onClick(screen)
        },

    ){
         Surface(
             color = color,
             shape = MaterialTheme.shapes.medium


         ) {
             Text(
                 modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                 text = screen.name.toUpperCase(Locale.current),
                 textAlign = TextAlign.Center,
                 fontWeight = fontWeight,
                 style = MaterialTheme.typography.button
             )
         }
    }
}

@Preview
@Composable
fun PreviewHome() {
    BadNutritionTheme {
        HomeContent()
    }
}

//@Preview
//@Composable
//fun PreviewSimpleCard(){
//    BadNutritionTheme {
//        SimpleNutritionCard(label = "Deeeeeeeeeez Nutzzzzzzz")
//    }
//}

@Preview
@Composable
fun PreviewStandardCard() {
    BadNutritionTheme {
        StandardNutritionCard(MockData().nutrientExample())
    }
}
//
//@Preview
//@Composable
//fun PreviewComplexCard() {
//    BadNutritionTheme{
//        ComplexNutritionCard(label = "Nutzzzzzzzzzzzzz")
//    }
//}