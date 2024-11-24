package com.ivan.mealapp.domain.models

data class Meal(
    val id: String,
    val name: String,
    val image: String,
    val preparationSteps: String? = null,
    val category: String? = null,
    val tags: List<String>? = null,
    val isFavourite: Boolean = false
) {
    val hasDetailedData: Boolean
        get() = !preparationSteps.isNullOrEmpty()
}
