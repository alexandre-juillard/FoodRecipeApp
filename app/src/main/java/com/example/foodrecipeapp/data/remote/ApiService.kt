package com.example.foodrecipeapp.data.remote

import com.example.foodrecipeapp.data.model.Recipe
import com.example.foodrecipeapp.data.model.RecipeResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

private const val BASE_URL = "https://food2fork.ca/api/recipe/"
private val json = Json {
    ignoreUnknownKeys = true
}

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()

interface FoodApiService {
    @GET("search/")
    suspend fun getRecipes(
        @Query("query") query: String = "",
        @Query("page") page: Int = 1,
        @Header("Authorization") authHeader: String = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
    ) : RecipeResponse

    @GET
    suspend fun getRecipesByUrl(
        @Url url: String,
        @Header("Authorization") authHeader: String = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
    ): RecipeResponse

    @GET("get/")
    suspend fun getRecipeDetails(
        @Query("id") id: Int,
        @Header("Authorization") authHeader: String = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
    ): Recipe
}

object FoodApi {
    val retrofitService: FoodApiService by lazy {
        retrofit.create(FoodApiService::class.java)
    }
}