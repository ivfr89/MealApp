package com.ivan.mealapp.domain

import com.ivan.mealapp.domain.fakes.FakeMealRepository
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.usecases.GetMealById
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMealByIdTest {

    private lateinit var fakeMealRepository: FakeMealRepository
    private lateinit var getMealById: GetMealById

    @BeforeTest
    fun setup() {
        fakeMealRepository = FakeMealRepository()
        getMealById = GetMealById(fakeMealRepository)
    }

    @Test
    fun `execute should return meal when repository succeeds`() = runBlocking {
        val meal = Meal(
            id = "1",
            name = "Pizza",
            category = "Seafood",
            image = "",
            tags = listOf("Italian", "Pizza"),
            preparationSteps = "Prepare the pizza",
            isFavourite = true
        )
        fakeMealRepository.setMeals(listOf(meal))
        val params = GetMealById.Params(id = "1")

        // Act
        val result = getMealById.execute(params)

        // Assert
        result.collect { either ->
            assertTrue(either is Either.Right)
            assertEquals(meal, either.b)
        }
    }

    @Test
    fun `execute should return failure when repository fails`() = runBlocking {
        // Arrange
        fakeMealRepository.setFailure()
        val params = GetMealById.Params(id = "123")

        // Act
        val result = getMealById.execute(params)

        // Assert
        result.collect { either ->
            assertTrue(either is Either.Left)
            assertEquals(Failure.UnknownError, either.a)
        }
    }
}
