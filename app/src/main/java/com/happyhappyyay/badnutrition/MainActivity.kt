package com.happyhappyyay.badnutrition

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.happyhappyyay.badnutrition.data.database.NutritionDatabase
import com.happyhappyyay.badnutrition.ui.home.Home
import com.happyhappyyay.badnutrition.ui.home.HomeViewModel
import com.happyhappyyay.badnutrition.ui.theme.BadNutritionTheme


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setVmPolicy(
            VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build()
        )
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