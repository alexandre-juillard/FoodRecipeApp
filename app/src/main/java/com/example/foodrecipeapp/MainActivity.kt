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
import com.example.foodrecipeapp.data.local.AppDatabase
import com.example.foodrecipeapp.data.remote.FoodApi
import com.example.foodrecipeapp.data.repository.RecipeRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Obtenir une instance de la base de données et du DAO
        val database = AppDatabase.getInstance(this)
        val recipeDao = database.recipeDao()
        // Obtenir l'instance de l'API
        val apiService = FoodApi.retrofitService

        // Créer le repository
        val repository = RecipeRepository(recipeDao, apiService)

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