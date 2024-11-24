package com.ivan.mealapp.data.remote.mapper

import com.ivan.mealapp.data.remote.models.MealDetailInfoEntity
import com.ivan.mealapp.data.remote.models.MealEntity
import com.ivan.mealapp.domain.models.Meal

internal fun MealEntity.toDomain() = Meal(
    id = id,
    name = name,
    image = image
)

internal fun MealDetailInfoEntity.toDomain() = Meal(
    id = id,
    name = name,
    image = image,
    preparationSteps = instructions,
    category = category,
    tags = tags?.split(STRING_SEPARATOR).orEmpty()
)

private const val STRING_SEPARATOR = ","