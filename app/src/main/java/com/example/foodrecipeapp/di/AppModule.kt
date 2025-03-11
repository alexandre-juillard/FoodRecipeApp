package com.example.foodrecipeapp.di

import android.content.Context
import com.example.foodrecipeapp.data.local.AppDatabase
import com.example.foodrecipeapp.data.remote.FoodApi
import com.example.foodrecipeapp.data.repository.RecipeRepository

/**
 * Classe simple pour fournir les dépendances
 */
object AppModule {

    fun provideRecipeRepository(context: Context): RecipeRepository {
        val database = AppDatabase.getInstance(context)
        val recipeDao = database.recipeDao()
        val apiService = FoodApi.retrofitService

        return RecipeRepository(recipeDao, apiService)
    }
}