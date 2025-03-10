package com.example.foodrecipeapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val pk: Int,
    val title: String,
    val publisher: String,
    @ColumnInfo(name = "featured_image") val featuredImage: String,
    val rating: Int,
    @ColumnInfo(name = "source_url") val sourceUrl: String,
    val description: String,
    @ColumnInfo(name = "cooking_instructions") val cookingInstructions: String?,
    val ingredients: String,
    @ColumnInfo(name = "date_added") val dateAdded: String,
    @ColumnInfo(name = "date_updated") val dateUpdated: String,
    @ColumnInfo(name = "last_cached") val lastCached: Long = System.currentTimeMillis()
)
