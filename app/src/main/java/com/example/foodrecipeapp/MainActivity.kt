package com.example.foodrecipeapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.foodrecipeapp.ui.screens.RecipeScreen
import com.example.foodrecipeapp.ui.theme.FoodRecipeAppTheme
import com.example.foodrecipeapp.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_FoodRecipeApp)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Initialisation du ViewModel
            val viewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

            RecipeScreen(viewModel) { selectedRecipe ->
                Log.d("RecipeClick", "Recette sélectionnée : ${selectedRecipe.title}" )

            }
        }
    }
}