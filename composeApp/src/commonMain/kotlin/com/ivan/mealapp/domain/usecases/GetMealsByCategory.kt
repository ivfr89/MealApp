package com.ivan.mealapp.domain.usecases

import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow

class GetMealsByCategory(
    private val mealRepository: MealRepository
) :
    UseCaseFlow<GetMealsByCategory.Params, List<Meal>> {

    override suspend fun execute(params: Params): Flow<Either<Failure, List<Meal>>> =
        mealRepository.getMealsByCategory(params.category)

    data class Params(
        val category: String
    )
}
