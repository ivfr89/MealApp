package com.ivan.mealapp.data.local

import com.ivan.mealapp.data.local.database.MealsDao
import com.ivan.mealapp.data.local.database.mapper.toDb
import com.ivan.mealapp.data.local.database.mapper.toDomain
import com.ivan.mealapp.data.local.database.utils.mapList
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.models.functional.toLeft
import com.ivan.mealapp.domain.models.functional.toRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MealLocalDataSourceImpl(
    private val mealsDao: MealsDao
) : MealLocalDataSource {
    override fun getMealsByCategory(category: String): Flow<Either<Failure, List<Meal>>> =
        mealsDao.getMealsByCategory(category)
            .mapList { it.toDomain() }

    override fun getMealById(id: String): Flow<Either<Failure, Meal>> =
        mealsDao.getMealById(id)
            .map {
                it?.toDomain()?.run {
                    takeIf { meal -> meal.hasDetailedData }?.toRight() ?: NoDetailedMeal.toLeft()
                } ?: Failure.ElementNotFound(NOT_FOUND_MESSAGE)
                    .toLeft()
            }

    override suspend fun saveMealsByCategory(meals: List<Meal>, category: String) {
        mealsDao.saveMeals(meals.map { it.copy(category = category).toDb() })
    }

    override suspend fun saveMeal(meal: Meal) {
        mealsDao.saveDetailMeal(meal.toDb())
    }

    override suspend fun countMeals(): Int =
        mealsDao.countMeals()

}

object NoDetailedMeal : Failure.CustomFailure()

private const val NOT_FOUND_MESSAGE = "Empty list"