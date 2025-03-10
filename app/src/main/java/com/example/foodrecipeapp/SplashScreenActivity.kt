package com.example.foodrecipeapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodrecipeapp.viewmodel.RecipeState
import com.example.foodrecipeapp.viewmodel.RecipeViewModel
import com.example.foodrecipeapp.data.factory.RecipeViewModelFactory
import com.example.foodrecipeapp.data.local.AppDatabase
import com.example.foodrecipeapp.data.remote.FoodApi
import com.example.foodrecipeapp.data.repository.RecipeRepository

class SplashScreenActivity : AppCompatActivity() {
    private val database by lazy { AppDatabase.getInstance(this) } // Obtenir l'instance de la DB
    private val recipeDao by lazy { database.recipeDao() } // Obtenir le DAO
    private val apiService by lazy { FoodApi.retrofitService } // Instance de l'API
    private val repository by lazy { RecipeRepository(recipeDao, apiService) }

    private val viewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Utilise un Handler pour retarder la transition
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Démarre MainActivity
            finish() // Termine SplashScreenActivity pour éviter de revenir en arrière
        }, 2000) // Délai de 2 secondes

        viewModel.getRecipes()

        val state = viewModel.recipeState
        when(state) {
            is RecipeState.Success -> {
                startMainActivity()
            }
            is RecipeState.Error -> {
                startMainActivity()
            }
            is RecipeState.Loading -> {  }
            else -> {}
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}