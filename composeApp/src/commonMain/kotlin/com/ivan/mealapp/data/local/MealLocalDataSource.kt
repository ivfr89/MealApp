package com.ivan.mealapp.data.local

import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import kotlinx.coroutines.flow.Flow

interface MealLocalDataSource {

    fun getMealsByCategory(category: String): Flow<Either<Failure, List<Meal>>>

    fun getMealById(id: String): Flow<Either<Failure, Meal>>

    suspend fun saveMeals(meals: List<Meal>)

    suspend fun saveMeal(meal: Meal)

    suspend fun countMeals(): Int

}
