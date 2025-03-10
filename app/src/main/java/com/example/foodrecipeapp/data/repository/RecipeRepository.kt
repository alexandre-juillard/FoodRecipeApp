package com.example.foodrecipeapp.data.repository

import com.example.foodrecipeapp.data.local.dao.RecipeDao
import com.example.foodrecipeapp.data.local.mapper.toEntity
import com.example.foodrecipeapp.data.local.mapper.toDomainModel
import com.example.foodrecipeapp.data.model.Recipe
import com.example.foodrecipeapp.data.remote.FoodApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val apiService: FoodApiService
) {
    suspend fun refreshRecipes(query: String) {
        try {
            val response = apiService.getRecipes(query)
            val entities = response.results.map { it.toEntity() }

            recipeDao.insertRecipes(entities)
            recipeDao.cleanOldRecipes(entities.map { it.pk })

        } catch (e: IOException) {
            if (recipeDao.getRecipesCount() == 0) throw e
        }
    }

    fun getCachedRecipes(query: String = ""): Flow<List<Recipe>> {
        return recipeDao.getRecipes(query).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    suspend fun getRecipesCount(): Int {
        return recipeDao.getRecipesCount()
    }
}