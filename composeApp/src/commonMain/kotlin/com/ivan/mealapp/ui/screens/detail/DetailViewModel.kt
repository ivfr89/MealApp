package com.ivan.mealapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.domain.usecases.GetMealById
import com.ivan.mealapp.domain.usecases.ToggleFavouriteMeal
import com.ivan.mealapp.ui.screens.HandleAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getMealById: GetMealById,
    private val toggleFavouriteMeal: ToggleFavouriteMeal
) : ViewModel(), HandleAction<DetailScreenActions> {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    override fun handleAction(action: DetailScreenActions) {
        when(action) {
            is DetailScreenActions.Initialize -> executeInitialize(action)
            is DetailScreenActions.ToggleFavourite -> executeToggleFavourite(action)
        }
    }
    
    private fun executeInitialize(action: DetailScreenActions.Initialize) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            getMealById(GetMealById.Params(action.id)).collect {
                it.map { meal ->
                    _state.value = UiState(loading = false, meal = meal)
                }.mapLeft {
                    _state.value = UiState(loading = false)
                }
            }
        }
    }

    private fun executeToggleFavourite(action: DetailScreenActions.ToggleFavourite) {
        _state.value.meal?.let {
            viewModelScope.launch {
                toggleFavouriteMeal(ToggleFavouriteMeal.Params(it))
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val meal: Meal? = null
    )
}