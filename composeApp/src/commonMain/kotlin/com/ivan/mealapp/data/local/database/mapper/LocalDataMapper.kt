package com.ivan.mealapp.data.local.database.mapper

import com.ivan.mealapp.data.local.database.models.MealDb
import com.ivan.mealapp.domain.models.Meal

internal fun MealDb.toDomain() = Meal(
    id = id,
    name = name,
    image = image,
    isFavourite = favourite,
    preparationSteps = preparationSteps,
    category = category,
    tags = tags?.split(STRING_SEPARATOR)
)

internal fun Meal.toDb() = MealDb(
    id = id,
    name = name,
    image = image,
    favourite = isFavourite,
    preparationSteps = preparationSteps,
    category = category,
    tags = tags?.joinToString(STRING_SEPARATOR)
)
private const val STRING_SEPARATOR = ","
