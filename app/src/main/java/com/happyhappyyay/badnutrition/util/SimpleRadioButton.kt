package com.happyhappyyay.badnutrition.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun SimpleRadioButton(
    modifier: Modifier = Modifier,
    label: String = "Button",
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier =
        modifier
            .clickable {
                onClick()
            }
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Text(modifier = Modifier.padding(end = 8.dp), text = label)
        Box(
            Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(
                    ButtonDefaults
                        .buttonColors()
                        .backgroundColor(enabled = true).value
                )
                .shadow(4.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(visible = isEnabled) {
                Box(
                    Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.secondary)
                )
            }
        }
    }
}
