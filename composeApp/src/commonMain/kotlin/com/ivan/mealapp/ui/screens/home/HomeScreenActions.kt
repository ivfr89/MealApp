package com.ivan.mealapp.ui.screens.home

import com.ivan.mealapp.ui.screens.Action

sealed interface HomeScreenActions : Action {
    data class Initialize(val type: CategoryType) : HomeScreenActions
}

enum class CategoryType(val value: String) {
    Seafood("Seafood")
}
