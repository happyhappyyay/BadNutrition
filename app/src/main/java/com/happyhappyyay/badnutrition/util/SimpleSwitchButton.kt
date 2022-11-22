package com.happyhappyyay.badnutrition.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme
import com.happyhappyyay.badnutrition.ui.theme.roundedLeftShape
import com.happyhappyyay.badnutrition.ui.theme.roundedRightShape

@Composable
fun SimpleSwitchButton(
    modifier: Modifier = Modifier,
    label1: String,
    label2: String,
    isFirstPressed: Boolean,
    onClick: () -> Unit
) {
    Row(modifier = modifier.padding(8.dp)) {
        SwitchItem(
            modifier = Modifier
                .weight(1f)
                .clip(roundedLeftShape),
            label = label1,
            isEnabled = !isFirstPressed
        ) {
            onClick()
        }
        SwitchItem(
            modifier = Modifier
                .weight(1f)
                .clip(roundedRightShape),
            label = label2,
            isEnabled = isFirstPressed
        ) {
            onClick()
        }
    }
}

@Composable
fun SwitchItem(
    modifier: Modifier = Modifier,
    label: String,
    isEnabled: Boolean,
    function: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isEnabled) MaterialTheme.colors.primary else Color.Gray,
        animationSpec = snap(125)
    )
    val mod = if (isEnabled) modifier.clickable { function() } else modifier
    Box(
        modifier = mod
            .background(color = color)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = label,
            color = if (isEnabled) MaterialTheme.colors.onPrimary else Color.LightGray
        )
    }
}

@Preview
@Composable
fun PreviewSimpleSwitchButton() {
    BadNutritionTheme {
        SimpleSwitchButton(
            label1 = "coolzzzzzzzzzzzzzzzzz",
            label2 = "lamzzzzzzzzzzzzzzze",
            isFirstPressed = true
        ) {}
    }
}

@Preview
@Composable
fun PreviewSwitchItem() {
    BadNutritionTheme {
        SwitchItem(label = "cool", isEnabled = false) {}
    }
}