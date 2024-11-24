package com.ivan.mealapp.ui.screens

interface HandleAction<Action> {
    fun handleAction(action: Action)
}
