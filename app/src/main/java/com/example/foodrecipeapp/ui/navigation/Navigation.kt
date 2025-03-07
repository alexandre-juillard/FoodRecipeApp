package com.example.foodrecipeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodrecipeapp.ui.screens.RecipeDetailScreen
import com.example.foodrecipeapp.ui.screens.RecipeScreen
import com.example.foodrecipeapp.viewmodel.RecipeViewModel

sealed class Screen(val route: String) {
    object RecipeList: Screen("recipeList")
    object RecipeDetail: Screen("recipeDetail/{recipeId}") {
        fun createRoute(recipeId: Int) = "recipeDetail/$recipeId"
    }
}

@Composable
fun FoodRecipeApp(viewModel: RecipeViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.RecipeList.route
    ) {
        composable(Screen.RecipeList.route) {
            RecipeScreen(
                viewModel = viewModel,
                onRecipeClick = { recipe ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipe.pk))
                }
            )
        }
        composable(
            route = Screen.RecipeDetail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) {
            backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: return@composable
            RecipeDetailScreen(recipeId = recipeId, viewModel = viewModel)
        }
    }
}