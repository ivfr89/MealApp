package com.ivan.mealapp.domain.models.functional

sealed class Failure {

    data object UnknownError : Failure()
    data class ElementNotFound(val reason: String?) : Failure()
    abstract class CustomFailure : Failure()
}
