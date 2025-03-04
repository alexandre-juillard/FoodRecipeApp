package com.example.foodrecipeapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodrecipeapp.data.model.Recipe

@Composable
fun RecipeDetailScreen(recipe: Recipe) {
    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = recipe.featuredImage,
            contentDescription = "Image de la recette",
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentScale = ContentScale.Crop
        )

        Text(recipe.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Note : ${recipe.rating}/10", fontSize = 16.sp, color = Color.Gray)
        Text("Ingrédients :", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))

        recipe.ingredients.forEach { ingredient ->
            Text("- $ingredient", modifier = Modifier.padding(start = 8.dp, top = 4.dp))
        }
    }
}
