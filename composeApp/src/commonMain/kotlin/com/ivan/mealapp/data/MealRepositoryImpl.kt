package com.ivan.mealapp.data

import com.ivan.mealapp.data.local.MealLocalDataSource
import com.ivan.mealapp.data.remote.MealRemoteDataSource
import com.ivan.mealapp.data.remote.mapper.toDomain
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.models.functional.flatMapLeft
import com.ivan.mealapp.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MealRepositoryImpl(
    private val remoteDataSource: MealRemoteDataSource,
    private val localDataSource: MealLocalDataSource,
) : MealRepository {

    override suspend fun getMealsByCategory(category: String): Flow<Either<Failure, List<Meal>>> =
        localDataSource.getMealsByCategory(category).map { localMeals ->
            localMeals.flatMapLeft {
                fetchAndSaveRemoteMeals(category)
            }
        }

    override suspend fun getMealById(id: String): Flow<Either<Failure, Meal>> =
        localDataSource.getMealById(id).map { localMeal ->
            localMeal.flatMapLeft {
                remoteDataSource.getMealById(id).map {
                    it.toDomain().also { localDataSource.saveMeal(it) }
                }
            }
        }

    override suspend fun toggleFavorite(meal: Meal): Either<Failure, Unit> {
        localDataSource.saveMeals(listOf(meal.copy(isFavourite = true)))
        return Either.Right(Unit)
    }


    private suspend fun fetchAndSaveRemoteMeals(category: String): Either<Failure, List<Meal>> =
        remoteDataSource.getMealsByCategory(category).map {
            it.meals.map { meal -> meal.toDomain() }
                .also { meals -> localDataSource.saveMeals(meals) }
        }
}
