package com.example.foodrecipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipeapp.data.model.Recipe
import com.example.foodrecipeapp.data.remote.FoodApi
import com.example.foodrecipeapp.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

//etats possibles à la recup des recettes
sealed interface RecipeState {
    data class Success(val recipes: List<Recipe>, val count: Int) : RecipeState
    data class Error(val message : String) : RecipeState
    object Loading : RecipeState
    object Empty : RecipeState
}

sealed interface RecipeDetailState {
    object Loading : RecipeDetailState
    data class Success(val recipe: Recipe) : RecipeDetailState
    data class Error(val message: String) : RecipeDetailState
}

class RecipeViewModel(private val recipeRepository: RecipeRepository): ViewModel() {
    var nextPageUrl: String? = null
    var isFetching = false
    private var allRecipes = mutableListOf<Recipe>()
    private val _recipeDetailState = mutableStateOf<RecipeDetailState>(RecipeDetailState.Loading)
    val recipeDetailState: State<RecipeDetailState> = _recipeDetailState
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    var recipeState by mutableStateOf<RecipeState>(RecipeState.Loading)
        private set

    var searchQuery by mutableStateOf("") // ajout de la recherche
        private set

    init {
        getRecipes()
    }

    //reset de la liste des recettes
    fun updateSearchQuery(newQuery: String) {
        searchQuery = newQuery
        nextPageUrl = null
        allRecipes.clear()
        getRecipes()
    }

    fun getRecipes() {
        Log.d("Debug", "getRecipes()")
        if (isFetching || nextPageUrl == null && allRecipes.isNotEmpty()) return
        isFetching = true

        viewModelScope.launch {
            recipeState = RecipeState.Loading
           try {
              val response = if (nextPageUrl == null) {
                  FoodApi.retrofitService.getRecipes(
                      searchQuery.ifEmpty { "" })
              } else {
                  FoodApi.retrofitService.getRecipesByUrl(nextPageUrl!!)
              }

               // filtre les recettes par titre côté client
               val filteredRecipes = if (searchQuery.isNotEmpty()) {
                   response.results.filter { recipe ->
                       recipe.title.contains(searchQuery, ignoreCase = true)
                   }
               } else {
                   response.results
               }

               // gestion des etats selon la reponse
               if (filteredRecipes.isNotEmpty()) {
                   allRecipes.addAll(filteredRecipes) // ajoute les nouvelles recettes
                   recipeState = RecipeState.Success(allRecipes, response.count)
               } else if (response.count == 0 && allRecipes.isEmpty()) {
                   recipeState = RecipeState.Empty
               } else {
                   recipeState = RecipeState.Success(allRecipes, response.count) // aucune nouvelle recette
               }

               Log.d("API Response", response.toString())
               // met à jour l'URL de la page suivante
               nextPageUrl = response.next

           } catch (e: IOException) {
               Log.e("Error", "${e.message}", e)
               recipeState = RecipeState.Error("Connexion impossible. Vérifier votre connexion.")
           } finally {
               isFetching = false
           }
        }
    }

    private val _cachedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val cachedRecipes: StateFlow<List<Recipe>> = _cachedRecipes

    init {
        viewModelScope.launch {
            recipeRepository.getCachedRecipes().collect { recipes ->
                _cachedRecipes.value = recipes
            }
        }
    }

    fun loadMoreRecipes() {
        if (!isFetching && nextPageUrl != null) {
            Log.d("Pagination", "Chargement de la page suivante...")
            getRecipes() // pour charger la page suivante
        }
    }

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            _recipeDetailState.value = RecipeDetailState.Loading
            try {
                val response = FoodApi.retrofitService.getRecipeDetails(recipeId)
                _recipeDetailState.value = RecipeDetailState.Success(response)
            } catch (e: Exception) {
                _recipeDetailState.value = RecipeDetailState.Error("Erreur de chargement : Vérifier votre connexion.")
            }
        }
    }

    suspend fun refreshRecipesIfNeeded() {
        if (recipeRepository.getRecipesCount() == 0) { // Si le cache est vide
            refreshRecipes("") // Rafraîchir les recettes avec une requête vide
        }
    }

    private suspend fun refreshRecipes(query: String) {
        try {
            val response = recipeRepository.refreshRecipes(query)
            // Gérer la réponse si nécessaire
        } catch (e: IOException) {
            throw e // Propager l'erreur pour la gérer dans le composable
        }
    }

    fun showError(message: String) {
        _errorMessage.value = message
    }

    fun clearError() {
        _errorMessage.value = null
    }
}