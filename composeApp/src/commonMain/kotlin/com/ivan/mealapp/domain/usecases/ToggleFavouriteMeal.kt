package com.ivan.mealapp.domain.usecases

import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.repository.MealRepository

class ToggleFavouriteMeal(private val mealRepository: MealRepository) :
    UseCase<ToggleFavouriteMeal.Params, Unit> {

    override suspend fun execute(params: Params): Either<Failure, Unit> =
        mealRepository.toggleFavorite(params.meal)

    class Params(val meal: Meal)
}
