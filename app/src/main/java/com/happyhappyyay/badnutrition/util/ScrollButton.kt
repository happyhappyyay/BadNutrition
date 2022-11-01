package com.happyhappyyay.badnutrition.util

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

@Composable
fun ScrollButton(onClick: () -> Unit){
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        Alignment.BottomCenter
    ){
        Button(modifier = Modifier
            .shadow(8.dp, shape = CircleShape)
            .clip(shape = CircleShape)
            .size(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            onClick = { onClick() }) {
            Icon(
                Icons.Rounded.KeyboardArrowUp,
                ""
                )
        }
    }
}

@Preview
@Composable
fun PreviewScrollButton() {
    BadNutritionTheme{
        ScrollButton{}
    }
}