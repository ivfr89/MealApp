package com.ivan.mealapp.domain

import com.ivan.mealapp.domain.fakes.FakeMealRepository
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.usecases.ToggleFavouriteMeal
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ToggleFavouriteMealTest {

    private lateinit var fakeMealRepository: FakeMealRepository
    private lateinit var toggleFavouriteMeal: ToggleFavouriteMeal

    @BeforeTest
    fun setup() {
        fakeMealRepository = FakeMealRepository()
        toggleFavouriteMeal = ToggleFavouriteMeal(fakeMealRepository)
    }

    @Test
    fun `execute should toggle meal should success`() = runBlocking {
        val meal = Meal(
            id = "1",
            name = "Pizza",
            category = "Seafood",
            image = "",
            tags = listOf("Italian", "Pizza"),
            preparationSteps = "Prepare the pizza",
            isFavourite = false
        )

        fakeMealRepository.toggleFavorite(meal)
        val params = ToggleFavouriteMeal.Params(meal = meal)

        // Act
        val result = toggleFavouriteMeal.execute(params)

        // Assert
        assertTrue(result is Either.Right)
    }

    @Test
    fun `execute should return failure when repository fails`() = runBlocking {
        fakeMealRepository.setFailure()
        val meal = Meal(
            id = "1",
            name = "Pizza",
            category = "Seafood",
            image = "",
            tags = listOf("Italian", "Pizza"),
            preparationSteps = "Prepare the pizza",
            isFavourite = false
        )

        val params = ToggleFavouriteMeal.Params(meal = meal)

        // Act
        val result = toggleFavouriteMeal.execute(params)

        // Assert
        assertTrue(result is Either.Left)
        assertEquals(Failure.UnknownError, result.a)
    }
}
