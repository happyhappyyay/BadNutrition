package com.happyhappyyay.badnutrition.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.smallRoundShape

@Composable
fun SimpleCheckBox(
    modifier: Modifier = Modifier,
    label: String = "Button",
    isChecked: Boolean,
    onClick: () -> Unit
) {
    Row(modifier =
    modifier
        .clickable {
            onClick()
        }
        .padding(horizontal = 8.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Text(
            label,
            modifier = Modifier.padding(8.dp),
        )
        Box(
            modifier = Modifier
                .padding(8.dp)
                .height(20.dp)
                .width(20.dp)
                .clip(smallRoundShape)
                .border(
                    1.5.dp,
                    MaterialTheme.colors.onBackground,
                    smallRoundShape
                )
                .background(if(isChecked)MaterialTheme.colors.secondary else Color.Transparent)
        ) {
            androidx.compose.animation.AnimatedVisibility(visible = isChecked) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "",
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCheckBox(){
    BadNutritionTheme {
        SimpleCheckBox(isChecked = false) {
        }
    }
}