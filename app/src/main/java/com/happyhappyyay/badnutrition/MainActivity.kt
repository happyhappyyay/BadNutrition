package com.happyhappyyay.badnutrition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.happyhappyyay.badnutrition.database.NutritionDatabase
import com.happyhappyyay.badnutrition.home.Home
import com.happyhappyyay.badnutrition.home.HomeViewModel
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = this.application
        val database = NutritionDatabase.getInstance(this)
        viewModel = HomeViewModel(
            application = application,
            database = database
        )
        setContent {
            BadNutritionTheme {
                // A surface container using the 'background' color from the theme
                Home(viewModel)
            }
        }
    }
}