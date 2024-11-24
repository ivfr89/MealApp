package com.ivan.mealapp.ui.screens.detail

import com.ivan.mealapp.ui.screens.Action

sealed interface DetailScreenActions : Action {
    data class Initialize(val id: String) : DetailScreenActions
    data object ToggleFavourite : DetailScreenActions
}

