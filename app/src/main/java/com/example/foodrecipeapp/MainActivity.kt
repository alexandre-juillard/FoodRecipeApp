package com.example.foodrecipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodrecipeapp.data.factory.RecipeViewModelFactory
import com.example.foodrecipeapp.ui.theme.FoodRecipeAppTheme
import com.example.foodrecipeapp.ui.navigation.FoodRecipeApp
import com.example.foodrecipeapp.viewmodel.RecipeViewModel
import com.example.foodrecipeapp.di.AppModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Créer le repository
        val repository = AppModule.provideRecipeRepository(applicationContext)

        setTheme(R.style.Theme_FoodRecipeApp)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodRecipeAppTheme {
                val viewModel: RecipeViewModel = viewModel(
                    factory = RecipeViewModelFactory(repository)
                )
                FoodRecipeApp(viewModel = viewModel)
            }
        }
    }
}