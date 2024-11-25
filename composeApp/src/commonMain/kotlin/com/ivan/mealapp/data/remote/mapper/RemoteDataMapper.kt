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

internal fun Meal.toDetailEntity() = MealDetailInfoEntity(
    id = id,
    name = name,
    image = image,
    instructions = preparationSteps.orEmpty(),
    category = category.orEmpty(),
    tags = tags?.joinToString(STRING_SEPARATOR).orEmpty()
)

internal fun Meal.toEntity() = MealEntity(
    id = id,
    name = name,
    image = image,
)

private const val STRING_SEPARATOR = ","