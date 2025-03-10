package com.example.foodrecipeapp.data.local.mapper

import com.example.foodrecipeapp.data.local.entity.RecipeEntity
import com.example.foodrecipeapp.data.model.Recipe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Recipe.toEntity(): RecipeEntity = RecipeEntity(
    pk = this.pk,
    title = this.title,
    publisher = this.publisher,
    featuredImage = this.featuredImage,
    rating = this.rating,
    sourceUrl = this.sourceUrl,
    description = this.description,
    cookingInstructions = this.cookingInstructions,
    ingredients = Json.encodeToString(this.ingredients),
    dateAdded = this.dateAdded,
    dateUpdated = this.dateUpdated
)

fun RecipeEntity.toDomainModel(): Recipe = Recipe(
    pk = this.pk,
    title = this.title,
    publisher = this.publisher,
    featuredImage = this.featuredImage,
    rating = this.rating,
    sourceUrl = this.sourceUrl,
    description = this.description,
    cookingInstructions = this.cookingInstructions,
    ingredients = Json.decodeFromString(this.ingredients),
    dateAdded = this.dateAdded,
    dateUpdated = this.dateUpdated,
    longDateAdded = this.lastCached,
    longDateUpdated = this.lastCached
)