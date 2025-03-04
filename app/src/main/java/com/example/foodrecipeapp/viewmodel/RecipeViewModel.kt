package com.example.foodrecipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipeapp.data.model.Recipe
import com.example.foodrecipeapp.data.remote.FoodApi
import kotlinx.coroutines.launch
import java.io.IOException

//etats possibles à la recup des recettes
sealed interface RecipeState {
    data class Success(val recipes: List<Recipe>, val count: Int) : RecipeState
    data class Error(val message : String) : RecipeState
    object Loading : RecipeState
    object Empty : RecipeState
}

class RecipeViewModel: ViewModel() {
    var nextPageUrl: String? = null
    var isFetching = false
    private var allRecipes = mutableListOf<Recipe>()

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
//        Log.d("Debug", "getRecipes()")
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

    fun loadMoreRecipes() {
        if (!isFetching && nextPageUrl != null) {
            getRecipes() // pour charger la page suivante
        }
    }
}