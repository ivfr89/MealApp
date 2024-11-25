package com.ivan.mealapp.domain

import com.ivan.mealapp.domain.fakes.FakeMealRepository
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.usecases.GetMealsByCategory
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMealsByCategoryTest {

    private lateinit var fakeMealRepository: FakeMealRepository
    private lateinit var getMealsByCategory: GetMealsByCategory

    @BeforeTest
    fun setup() {
        fakeMealRepository = FakeMealRepository()
        getMealsByCategory = GetMealsByCategory(fakeMealRepository)
    }

    @Test
    fun `execute should return meals when repository succeeds`() = runBlocking {
        val meals = listOf(
            Meal(
                id = "1",
                name = "Pizza",
                category = "Seafood",
                image = "",
                tags = listOf("Italian", "Pizza"),
                preparationSteps = "Prepare the pizza",
                isFavourite = true
            ),
            Meal(
                id = "2",
                name = "Pasta",
                category = "Seafood",
                image = "",
                tags = listOf("Italian", "Pasta"),
                preparationSteps = "Prepare the pasta",
                isFavourite = false
            )
        )
        fakeMealRepository.setMeals(meals)
        val params = GetMealsByCategory.Params(category = "Seafood")

        // Act
        val result = getMealsByCategory.execute(params)

        // Assert
        result.collect { either ->
            assertTrue(either is Either.Right)
            assertEquals(2, either.b.size)
        }
    }

    @Test
    fun `execute should return failure when repository fails`() = runBlocking {
        // Arrange
        fakeMealRepository.setFailure()
        val params = GetMealsByCategory.Params(category = "Italian")

        // Act
        val result = getMealsByCategory.execute(params)

        // Assert
        result.collect { either ->
            assertTrue(either is Either.Left)
            assertEquals(Failure.UnknownError, either.a)
        }
    }
}
