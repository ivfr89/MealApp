package com.ivan.mealapp.domain.fakes

import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMealRepository : MealRepository {

    private var shouldFail: Boolean = false
    private var mealsToReturn: List<Meal> = emptyList()
    private var favouriteMeals: MutableSet<Meal> = mutableSetOf()

    fun setMeals(meals: List<Meal>) {
        shouldFail = false
        mealsToReturn = meals
    }

    fun setFailure() {
        shouldFail = true
    }

    override suspend fun getMealById(id: String): Flow<Either<Failure, Meal>> {
        return flow {
            if (shouldFail) {
                emit(Either.Left(Failure.UnknownError))
            } else {
                emit(Either.Right(mealsToReturn.first { it.id == id }))
            }
        }
    }

    override suspend fun getMealsByCategory(category: String): Flow<Either<Failure, List<Meal>>> {
        return flow {
            if (shouldFail) {
                emit(Either.Left(Failure.UnknownError))
            } else {
                emit(Either.Right(mealsToReturn.filter { it.category == category }))
            }
        }
    }

    override suspend fun toggleFavorite(meal: Meal): Either<Failure, Unit> {
        return if (shouldFail) {
            Either.Left(Failure.UnknownError)
        } else {
            if (favouriteMeals.contains(meal)) {
                favouriteMeals.remove(meal)
            } else {
                favouriteMeals.add(meal)
            }
            Either.Right(Unit)
        }
    }
}
