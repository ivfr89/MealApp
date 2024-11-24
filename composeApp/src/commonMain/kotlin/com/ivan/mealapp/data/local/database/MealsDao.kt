package com.ivan.mealapp.data.local.database

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ivan.mealapp.data.local.database.models.MealDb
import kotlinx.coroutines.flow.Flow

@Dao
interface MealsDao {

    @Query("SELECT * FROM MealDb WHERE category = :category")
    fun getMealsByCategory(category: String): Flow<List<MealDb>>

    @Query("SELECT * FROM MealDb WHERE id = :id")
    fun getMealById(id: String): Flow<MealDb?>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMeals(meals: List<MealDb>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDetailMeal(meals: MealDb)

    @Query("SELECT COUNT(id) FROM MealDb")
    suspend fun countMeals(): Int

}