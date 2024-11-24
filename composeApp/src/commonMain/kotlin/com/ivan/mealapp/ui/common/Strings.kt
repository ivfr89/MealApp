package com.ivan.mealapp.ui.common

object LocalizedStrings {
    private val translations: Map<Language, Map<String, String>> = mapOf(
        Language.English to mapOf(
            "app_name" to "Meal App",
            "back" to "Go back",
            "favorite" to "Favorite",
            "seafood" to "Seafood",
            "chicken" to "Chicken",
            "starter" to "Starter",
            "breakfast" to "Breakfast",
            "category" to "Category",
            "howto" to "How to prepare",
            "tags" to "Tags",
        )
    )

    fun getString(key: String, language: Language = Language.English): String {
        return translations[language]?.get(key) ?: "String not found"
    }

    enum class Language(val value: String) {
        English("en"),
    }
}
