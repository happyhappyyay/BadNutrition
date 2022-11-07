package com.happyhappyyay.badnutrition.home

import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.widget.ContentLoadingProgressBar

@Composable
fun Barcode(){
    Box(modifier =
    Modifier.padding(8.dp)
        .border(1.dp, Color.Gray)
        .background(Color.Black)
        .fillMaxHeight(.935f)
        .fillMaxWidth()){
        Text("0001010101000100010101010001010010101",
        style = MaterialTheme.typography.h1,
        textAlign = TextAlign.Center,
            color = Color.Green
        )
    }
}