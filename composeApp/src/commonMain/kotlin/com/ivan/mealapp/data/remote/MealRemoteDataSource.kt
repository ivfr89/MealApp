package com.ivan.mealapp.data.remote

import com.ivan.mealapp.data.remote.models.MealDetailInfoEntity
import com.ivan.mealapp.data.remote.models.MealResultEntity
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure

interface MealRemoteDataSource {
    suspend fun getMealsByCategory(category: String): Either<Failure, MealResultEntity>

    suspend fun getMealById(id: String): Either<Failure, MealDetailInfoEntity>
}
