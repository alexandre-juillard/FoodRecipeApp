package com.example.foodrecipeapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Recipe>

)
