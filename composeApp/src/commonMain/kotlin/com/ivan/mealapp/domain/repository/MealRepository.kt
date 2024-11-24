package com.ivan.mealapp.domain.repository

import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    suspend fun getMealsByCategory(category: String): Flow<Either<Failure, List<Meal>>>

    suspend fun getMealById(id: String): Flow<Either<Failure, Meal>>

    suspend fun toggleFavorite(meal: Meal): Either<Failure, Unit>
}
