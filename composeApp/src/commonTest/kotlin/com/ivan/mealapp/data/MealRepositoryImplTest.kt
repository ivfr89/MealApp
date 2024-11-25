package com.ivan.mealapp.data

import com.ivan.mealapp.data.fakes.FakeMealLocalDataSource
import com.ivan.mealapp.data.fakes.FakeMealRemoteDataSource
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MealRepositoryImplTest {

    private lateinit var remoteDataSource: FakeMealRemoteDataSource
    private lateinit var localDataSource: FakeMealLocalDataSource
    private lateinit var repository: MealRepositoryImpl

    @BeforeTest
    fun setup() {
        remoteDataSource = FakeMealRemoteDataSource()
        localDataSource = FakeMealLocalDataSource()
        repository = MealRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getMealsByCategory should return local meals if available`() = runBlocking {
        // Arrange
        val meals = listOf(
            Meal(
                id = "1",
                name = "Pizza",
                category = "Seafood",
                image = "",
                tags = listOf("Italian", "Pizza"),
                preparationSteps = "Prepare the pizza",
                isFavourite = true
            )
        )
        localDataSource.setMealsByCategory("Italian", meals)

        // Act
        val result = repository.getMealsByCategory("Italian")

        // Assert
        result.collect { either ->
            assertTrue(either is Either.Right)
            assertEquals(meals, either.b)
        }
    }

    @Test
    fun `getMealsByCategory should fetch remote meals if local meals are unavailable`() =
        runBlocking {
            // Arrange
            val remoteMeals = listOf(
                Meal(
                    id = "1",
                    name = "Pizza",
                    image = "",
                    isFavourite = false
                )
            )
            remoteDataSource.setMeals(remoteMeals)

            // Act
            val result = repository.getMealsByCategory("Italian")

            // Assert
            result.collect { either ->
                assertTrue(either is Either.Right)
                assertEquals(remoteMeals, either.b)
            }
        }

    @Test
    fun `getMealById should return local meal if it has detailed data`() = runBlocking {
        // Arrange
        val meal = Meal(
            id = "1",
            name = "Pizza",
            category = "Seafood",
            image = "",
            tags = listOf("Italian", "Pizza"),
            preparationSteps = "Prepare the pizza",
            isFavourite = true
        )

        localDataSource.setMealById("1", meal)

        // Act
        val result = repository.getMealById("1")

        // Assert
        result.collect { either ->
            assertTrue(either is Either.Right)
            assertEquals(meal, either.b)
        }
    }

    @Test
    fun `getMealById should fetch remote meal if local meal has no detailed data`() = runBlocking {
        // Arrange
        val localMeal = Meal(
            id = "1",
            name = "Pizza",
            image = "",
            preparationSteps = null, // Local meal has incomplete data
            category = null,
            tags = null,
            isFavourite = false,
        )

        val remoteMeal = Meal(
            id = "1",
            name = "Pizza",
            category = "Seafood",
            image = "",
            tags = listOf("Italian", "Pizza"),
            preparationSteps = "Prepare the pizza",
            isFavourite = false
        )

        localDataSource.setMealById("2", localMeal)
        remoteDataSource.setMealById(remoteMeal)

        // Act
        val result = repository.getMealById("2")

        // Assert
        result.collect { either ->
            assertTrue(either is Either.Right)
            assertEquals(remoteMeal, either.b)
        }
    }

    @Test
    fun `getMealById should fetch remote meal if not found locally`() = runBlocking {
        // Arrange
        val remoteMeal = Meal(
            id = "3",
            name = "Pizza",
            category = "Seafood",
            image = "",
            tags = listOf("Italian", "Pizza"),
            preparationSteps = "Prepare the pizza",
            isFavourite = false
        )

        remoteDataSource.setMealById(remoteMeal)

        // Act
        val result = repository.getMealById("3")

        // Assert
        result.collect { either ->
            assertTrue(either is Either.Right)
            assertEquals(remoteMeal, either.b)
        }
    }

    @Test
    fun `toggleFavorite should update local meal`() = runBlocking {
        // Arrange
        val meal = Meal(
            id = "1",
            name = "Pizza",
            category = "Seafood",
            image = "",
            tags = listOf("Italian", "Pizza"),
            preparationSteps = "Prepare the pizza",
            isFavourite = false
        )

        localDataSource.setMealById("1", meal)

        // Act
        val result = repository.toggleFavorite(meal)

        // Assert
        assertTrue(result is Either.Right)
        val updatedMeal = localDataSource.getMealById("1").firstOrNull()
        assertTrue(updatedMeal is Either.Right)
        assertEquals(true, updatedMeal.b.isFavourite)
    }
}
