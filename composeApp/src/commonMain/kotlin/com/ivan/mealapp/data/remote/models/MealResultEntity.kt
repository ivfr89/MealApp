package com.ivan.mealapp.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealResultEntity(
    val meals : List<MealEntity>
)

@Serializable
data class MealDetailInfoResultEntity(
    val meals : List<MealDetailInfoEntity>
)

@Serializable
data class MealEntity(
    @SerialName("idMeal") val id: String,
    @SerialName("strMeal") val name: String,
    @SerialName("strMealThumb") val image: String
)


@Serializable
data class MealDetailInfoEntity(
    @SerialName("idMeal") val id: String,
    @SerialName("strMeal") val name: String,
    @SerialName("strMealThumb") val image: String,
    @SerialName("strCategory") val category: String,
    @SerialName("strInstructions") val instructions: String,
    @SerialName("strTags") val tags: String? = null,
)
