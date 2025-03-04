package com.example.foodrecipeapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val pk: Int,
    val title: String,
    val publisher: String,

    @SerialName("featured_image")
    val featuredImage: String,

    val rating: Int,

    @SerialName("source_url")
    val sourceUrl: String,

    val description: String,

    @SerialName("cooking_instructions")
    val cookingInstructions: String?,

    val ingredients: List<String>,

    @SerialName("date_added")
    val dateAdded: String,

    @SerialName("date_updated")
    val dateUpdated: String,

    @SerialName("long_date_added")
    val longDateAdded: Long,

    @SerialName("long_date_updated")
    val longDateUpdated: Long
)
