package com.ivan.mealapp.data.fakes

import com.ivan.mealapp.data.local.MealLocalDataSource
import com.ivan.mealapp.data.local.NoDetailedMeal
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMealLocalDataSource : MealLocalDataSource {

    private var mealsByCategory: Map<String, List<Meal>> = emptyMap()
    private var mealById: Map<String, Meal> = emptyMap()

    fun setMealsByCategory(category: String, meals: List<Meal>) {
        mealsByCategory = mealsByCategory + (category to meals)
    }

    fun setMealById(id: String, meal: Meal) {
        mealById = mealById + (id to meal)
    }

    override fun getMealsByCategory(category: String): Flow<Either<Failure, List<Meal>>> = flow {
            emit(
                if (mealsByCategory.containsKey(category)) {
                    Either.Right(mealsByCategory[category]!!)
                } else {
                    Either.Left(Failure.ElementNotFound(EMPTY_LIST_MESSAGE))
                }
            )
    }

    override fun getMealById(id: String): Flow<Either<Failure, Meal>> = flow {
        val meal = mealById[id]
        if (meal != null) {
            if (meal.hasDetailedData) {
                emit(Either.Right(meal))
            } else {
                emit(Either.Left(NoDetailedMeal))
            }
        } else {
            emit(Either.Left(Failure.ElementNotFound(NOT_FOUND_MESSAGE)))
        }
    }

    override suspend fun saveMealsByCategory(meals: List<Meal>, category: String) {
        mealsByCategory = mealsByCategory + (category to meals)
    }

    override suspend fun saveMeal(meal: Meal) {
        mealById = mealById + (meal.id to meal)
    }

    override suspend fun countMeals(): Int = 0
}

private const val EMPTY_LIST_MESSAGE = "Empty list"
private const val NOT_FOUND_MESSAGE = "Empty list"