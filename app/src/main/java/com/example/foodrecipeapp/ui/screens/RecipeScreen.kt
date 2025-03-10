package com.example.foodrecipeapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodrecipeapp.data.model.Recipe
import com.example.foodrecipeapp.ui.components.RecipeItem
import com.example.foodrecipeapp.ui.components.SearchBar
import com.example.foodrecipeapp.viewmodel.RecipeState
import com.example.foodrecipeapp.viewmodel.RecipeViewModel
import java.io.IOException

@SuppressLint("ResourceAsColor")
@Composable
fun RecipeScreen(viewModel: RecipeViewModel, onRecipeClick: (Recipe) -> Unit) {

    val state = viewModel.recipeState
    val searchQuery = viewModel.searchQuery
    val listState = rememberLazyListState()
    val recipes by viewModel.cachedRecipes.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        try {
            viewModel.refreshRecipesIfNeeded()
        } catch (e: IOException) {
            if (recipes.isEmpty()) {
                viewModel.showError("Pas de connexion et pas de cache")
            }
        }
    }

    if (!errorMessage.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Erreur") },
            text = { Text(errorMessage!!) },
            confirmButton = {
                Button(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }

    // détecter la fin de la liste des recettes
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty() && visibleItems.last().index >= listState.layoutInfo.totalItemsCount - 1) {
                    viewModel.loadMoreRecipes() // charge la page suivante
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFA500))
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChanged = { viewModel.updateSearchQuery(it) },
            onSearch = { viewModel.getRecipes() },
            onFilterClick = { filter ->
                viewModel.updateSearchQuery(filter)
                viewModel.getRecipes()
            }
        )

        when (state) {
            is RecipeState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            is RecipeState.Success -> {
                val recipes = state.recipes
                LazyColumn(
                    state = listState, // Associe l'état de la liste
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recipes) { recipe ->
                        RecipeItem(recipe = recipe, onClick = { onRecipeClick(recipe) })
                    }

                    if (viewModel.nextPageUrl == null && recipes.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Il n'y a pas d'autres recettes pour le moment.",
                                    color = Color.Black)
                            }
                        }
                    }

                    if (viewModel.isFetching) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
            is RecipeState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Padding général pour l'ensemble du contenu
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Texte d'erreur
                    Text(
                        text = "Erreur : ${state.message}",
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )

                    // Bouton d'actualisation
                    Button(
                        onClick = { viewModel.getRecipes() },
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
            is RecipeState.Empty -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Aucun résultat",
                        tint = Color.Black,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Aucune recette trouvée pour votre recherche.",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
