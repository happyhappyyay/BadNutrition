package com.happyhappyyay.badnutrition.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.happyhappyyay.badnutrition.R
import com.happyhappyyay.badnutrition.calendar.Calendar
import com.happyhappyyay.badnutrition.data.MockData
import com.happyhappyyay.badnutrition.data.nutrient.Nutrient
import com.happyhappyyay.badnutrition.charts.Chart
import com.happyhappyyay.badnutrition.charts.ChartType
import com.happyhappyyay.badnutrition.util.adjustDate
import com.happyhappyyay.badnutrition.util.currentDayString
import com.happyhappyyay.badnutrition.ui.theme.*
import com.happyhappyyay.badnutrition.util.ScrollButton
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class HomeStyle {
    Simple, Standard, Complex
}

enum class HomeType {
    Nutrition, Food
}

enum class TimeSpanUnit {
    All, Day, Week, Month, Year, Special
}

val adequateRangeColor = Color(0,255,0, 165)
val deficientRangeColor = Color(255,0,0,192)
val emptyBarColor = Color(201,201,201)

const val horGestureThreshold = 50

@Composable
fun Home(viewModel: HomeViewModel){
    Scaffold() {
        HomeContent()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent() {
    var style by remember { mutableStateOf(HomeStyle.Standard)}
    var type by remember { mutableStateOf(HomeType.Nutrition)}
    var dismiss by rememberSaveable { mutableStateOf(true) }
    var date by rememberSaveable { mutableStateOf(currentDayString())}

    AnimatedVisibility(visible = !dismiss) {
        Dialog(onDismissRequest = { dismiss = true }) {
            Calendar(date = date, setDate = {
                date = it
                dismiss = true
            })
        }
    }
    BackdropScaffold(
        modifier = Modifier,
        appBar = { NutritionAppBar() },
        backLayerContent = { HomeBackLayer() },
        frontLayerContent = { HomeFrontLayer(dismiss = { dismiss = !dismiss}, setType = {type = it}, style = style, type = type, date = date, setDate = {date = it}) },
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
fun HomeFrontLayer(
    dismiss: () -> Unit,
    setType: (HomeType) -> Unit,
    style: HomeStyle,
    type: HomeType,
    setDate: (String) -> Unit,
    date: String
) {
    var direction = 0
    var nDate by remember { mutableStateOf(date)}

    Box(modifier = Modifier.pointerInput(Unit){
        detectDragGestures (
            onDrag = { change, dragAmount ->
            change.consumeAllChanges()
            val (x,y) = dragAmount
            if(abs(x) - horGestureThreshold > abs(y)){
                direction = if(x > 0) -1 else 1
            }
        },
            onDragEnd = {
                nDate = adjustDate(date = nDate, direction)
                setDate(nDate)
        })
    }) {
        ScreenHeader(type = type, style = style, date = date, setType = setType, dismiss = dismiss)
    }
}
@Composable
fun FoodHome(dismiss: () -> Unit){
    Foods()
}

@Composable
fun ScreenHeader(
    type: HomeType,
    setType: (HomeType) -> Unit,
    dismiss: () -> Unit,
    style: HomeStyle,
    date: String
){
        when(type) {
            HomeType.Food -> FoodScreen(date, setType,dismiss)
            else -> NutritionHome(date, style, setType = setType, dismiss)
        }
}

@Composable
fun NutritionHome(date: String, style: HomeStyle, setType: (HomeType) -> Unit, dismiss: () -> Unit){
    val list = MockData().createNutritionList()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showButton by remember{
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp),
        state = listState
    ){
        item { ScreenHeading(type = HomeType.Nutrition,setType, dismiss) }
        item {
//            Chart(ChartTypes.Line, MockData().nutrientChartPoints, "Summary of $date")
            Chart(Modifier.height(225.dp), type = ChartType.Bar, data = MockData().pointsFromNutrients(list), heading = "Summary of $date")

        }
        itemsIndexed(list) { ind, nutritionItem ->
            when(style) {
                HomeStyle.Simple -> SimpleNutritionCard(nutritionItem)
                HomeStyle.Standard -> StandardNutritionCard(nutritionItem, GraphBarColors[ind])
                HomeStyle.Complex -> ComplexNutritionCard(nutritionItem)
            }
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

}

@Composable
fun SimpleNutritionCard(nutrient: Nutrient) {
    val percentage = Math.round((nutrient.value/(nutrient.goal.min * 1F))*99)
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
                        .background(emptyBarColor)
                        .align(Alignment.CenterVertically)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(maxWidth * 1F)
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(if (!overMax && percentage > 50) adequateRangeColor else deficientRangeColor)
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
fun StandardNutritionCard(nutrient: Nutrient, color: Color){
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
        shape = Shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight(),
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = Shapes.large,
                color = color,
                elevation = 2.dp
            ) {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp))
            }
                Row (Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
//                        Surface(
//                            modifier = Modifier.fillMaxWidth(.5f),
//                            shape = RectangleShape,
//                            color = color,
//                            elevation = 2.dp
//                        ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                            .fillMaxWidth(.5F),
                        text = label,
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Left
                    )
//                        }
//                    Surface(
//                        modifier = Modifier.weight(1.25f),
//                        shape = halfRoundedShapeLeftDeep,
//                        color = color,
//                        elevation = 4.dp
//                    ) {
                        Text(
                            text = "$zero$percentage%",
                            modifier = Modifier.padding(top = 0.dp, end = 8.dp),
                            style = MaterialTheme.typography.h5,
                            textAlign = TextAlign.Right
                        )
//                    }
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp),
                        text = when {
                            nutrient.goal.max < 0 -> "${nutrient.goal.min}${nutrient.measurement} min"
                            nutrient.goal.min <= 0 -> "${nutrient.goal.max}${nutrient.measurement} max"
                            else -> "${nutrient.goal.min}${nutrient.measurement} - ${nutrient.goal.max}${nutrient.measurement}"
                        }

                    )
//                    Surface(
//                        modifier = Modifier.fillMaxWidth(.8f),
//                        shape = halfRoundedShapeLeftDeep,
//                        color = color
//                    ) {
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            text = "${nutrient.value}${nutrient.measurement}",
                            textAlign = TextAlign.Right
                        )
//                    }
                }
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp, 8.dp, 16.dp, 8.dp)
                                .weight(.95f)
                                .height(14.dp)
                                .clip(CircleShape)
                                .background(emptyBarColor)
                                .border(BorderStroke(1.dp, Color.Gray), shape = CircleShape)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(0.dp, 1.dp)
                                    .fillMaxWidth(maxWidth * .989F)
                                    .height(12.dp)
                                    .clip(CircleShape)
                                    .background(if (!overMax && percentage > 50) adequateRangeColor else deficientRangeColor)
                            )
                        }
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .weight(.05f)
                            .padding(8.dp))
                    }
            }
        }
    }
}

@Composable
fun ScreenHeading(type: HomeType, setType: (HomeType)-> Unit, dismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .height(45.dp)
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 0.dp)
                .height(40.dp),
            text = if(type == HomeType.Food) "Food" else "Nutrition",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
        Row (modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp)){
            IconButton(onClick = { setType(HomeType.Nutrition)  },
                enabled = type == HomeType.Food) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.round_list_alt_black_32,
                    ),
                    ""
                )
            }
            IconButton(onClick = { setType(HomeType.Food) },
                enabled = type == HomeType.Nutrition) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.round_food_alt_black_32),
                    ""
                )
            }
        }
        Row(modifier = Modifier.padding(0.dp,0.dp,8.dp,0.dp), horizontalArrangement = Arrangement.End){
            IconButton(onClick = {dismiss()} ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_calendar_alt_black_32),
                    contentDescription = "Calendar"
                )
            }
        }
    }

}

@Composable
fun NutritionAppBar(){
    var selected by remember { mutableStateOf(TimeSpanUnit.Day)}
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
            NutritionFilterItem(screen = TimeSpanUnit.Day, selected = selected, onClick = {selected = it} )
            NutritionFilterItem(screen = TimeSpanUnit.Week, selected = selected, onClick = {selected = it})
            NutritionFilterItem(screen = TimeSpanUnit.Month,selected = selected, onClick = {selected = it})
            NutritionFilterItem(screen = TimeSpanUnit.Year, selected = selected, onClick = {selected = it})
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
fun NutritionFilterItem(screen: TimeSpanUnit, selected: TimeSpanUnit, modifier: Modifier = Modifier, onClick: (TimeSpanUnit)-> Unit) {
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
             shape = MaterialTheme.shapes.large,
             border = BorderStroke(0.25.dp, Color.DarkGray)


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
//        SimpleNutritionCard(label = "Nutzzzzzz")
//    }
//}

@Preview
@Composable
fun PreviewStandardCard() {
    BadNutritionTheme {
        StandardNutritionCard(MockData().nutrientExample(), Color.Cyan)
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