package com.ivan.mealapp.data.fakes

import com.ivan.mealapp.data.local.database.mapper.toDb
import com.ivan.mealapp.data.remote.MealRemoteDataSource
import com.ivan.mealapp.data.remote.mapper.toDetailEntity
import com.ivan.mealapp.data.remote.mapper.toEntity
import com.ivan.mealapp.data.remote.models.MealDetailInfoEntity
import com.ivan.mealapp.data.remote.models.MealDetailInfoResultEntity
import com.ivan.mealapp.data.remote.models.MealResultEntity
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure

class FakeMealRemoteDataSource : MealRemoteDataSource {

    private var remoteMeals: MealResultEntity = MealResultEntity(meals = emptyList())
    private var mealById: MealDetailInfoResultEntity =
        MealDetailInfoResultEntity(meals = emptyList())
    private var shouldFail: Boolean = false

    fun setMeals(meals: List<Meal>) {
        remoteMeals = remoteMeals.copy(meals.map { it.toEntity() })
    }

    fun setMealById(meal: Meal) {
        mealById = mealById.copy(listOf(meal.toDetailEntity()))
    }

    fun setFailure() {
        shouldFail = true
    }

    override suspend fun getMealsByCategory(category: String): Either<Failure, MealResultEntity> {
        return if (shouldFail) Either.Left(Failure.UnknownError)
        else Either.Right(remoteMeals)
    }

    override suspend fun getMealById(id: String): Either<Failure, MealDetailInfoEntity> {
        return if (shouldFail) Either.Left(Failure.UnknownError)
        else Either.Right(mealById.meals.first())
    }
}
