package com.example.foodrecipeapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.CircularProgressIndicator
import androidx.lifecycle.ViewModelProvider
import com.example.foodrecipeapp.viewmodel.RecipeState
import com.example.foodrecipeapp.viewmodel.RecipeViewModel

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Utilise un Handler pour retarder la transition
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Démarre MainActivity
            finish() // Termine SplashScreenActivity pour éviter de revenir en arrière
        }, 2000) // Délai de 3 secondes

        val viewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        viewModel.getRecipes()

        val state = viewModel.recipeState
        when(state) {
            is RecipeState.Success -> {
                startMainActivity()
            }
            is RecipeState.Error -> {
                startMainActivity() // Ou gérer l'erreur différemment
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