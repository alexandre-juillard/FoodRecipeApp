package com.example.foodrecipeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodrecipeapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' ORDER BY last_cached DESC")
    fun getRecipes(query: String): Flow<List<RecipeEntity>>

    @Query("DELETE FROM recipes WHERE pk NOT IN (:validIds)")
    suspend fun cleanOldRecipes(validIds: List<Int>)

    @Query("SELECT EXISTS(SELECT 1 FROM recipes WHERE pk = :id LIMIT 1)")
    suspend fun exists(id: Int): Boolean

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun getRecipesCount(): Int
}