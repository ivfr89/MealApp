package com.ivan.mealapp.domain.usecases

import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure


interface UseCase<Params, Return> {

    suspend operator fun invoke(params: Params): Either<Failure, Return> {
        return execute(params)
    }

    suspend fun execute(params: Params): Either<Failure, Return>
}
