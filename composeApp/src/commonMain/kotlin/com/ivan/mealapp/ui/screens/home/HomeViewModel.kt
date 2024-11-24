package com.ivan.mealapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.usecases.GetMealsByCategory
import com.ivan.mealapp.ui.screens.HandleAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMealsByCategory: GetMealsByCategory,
) : ViewModel(), HandleAction<HomeScreenActions> {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    override fun handleAction(action: HomeScreenActions) {
        when(action) {
            is HomeScreenActions.Initialize -> executeInitialize(action)
        }
    }

    private fun executeInitialize(action: HomeScreenActions.Initialize) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            getMealsByCategory(GetMealsByCategory.Params(action.type.value)).collect {
                it.map {
                    _state.value = UiState(loading = false, meals = it)
                }
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val meals: List<Meal> = emptyList()
    )
}
