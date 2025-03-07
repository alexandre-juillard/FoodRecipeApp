package com.example.foodrecipeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodrecipeapp.viewmodel.RecipeDetailState
import com.example.foodrecipeapp.viewmodel.RecipeViewModel

@Composable
fun RecipeDetailScreen(recipeId: Int, viewModel: RecipeViewModel) {
    val state = viewModel.recipeDetailState.value

    LaunchedEffect(recipeId) {
        viewModel.getRecipeDetails(recipeId)
    }

    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFA500))
                        .padding(top = 38.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)) {
        when (state) {
            is RecipeDetailState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is RecipeDetailState.Success -> {
                val recipe = state.recipe
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    AsyncImage(
                        model = recipe.featuredImage,
                        contentDescription = "Image de la recette",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        recipe.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
                    Spacer(
                        modifier = Modifier.height(8.dp))
                    Text(
                        "Updated ${recipe.dateUpdated} by ${recipe.publisher}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
                    Spacer(
                        modifier = Modifier.height(16.dp))
                    Text(
                        "Ingrédients :",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
                    LazyColumn {
                        items(recipe.ingredients) { ingredient ->
                            Text(
                                "- $ingredient",
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
                        }
                    }
                }

            }
            is RecipeDetailState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Padding général pour l'ensemble du contenu
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Texte d'erreur
                    Text(
                        text = state.message,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )

                    // Bouton d'actualisation
                    Button(
                        onClick = { viewModel.getRecipeDetails(recipeId) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF5F5F5)
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Rafraîchir",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Réessayer", color = Color.Black, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}
