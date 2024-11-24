package com.ivan.mealapp.domain.usecases

import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow

class GetMealById(
    private val mealRepository: MealRepository
) :
    UseCaseFlow<GetMealById.Params, Meal> {

    override suspend fun execute(params: Params): Flow<Either<Failure, Meal>> =
        mealRepository.getMealById(params.id)

    data class Params(
        val id: String
    )
}
