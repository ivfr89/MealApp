package com.ivan.mealapp.domain.usecases

import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import kotlinx.coroutines.flow.Flow


interface UseCaseFlow<Params, Return> {

    suspend operator fun invoke(params: Params): Flow<Either<Failure, Return>> {
        return execute(params)
    }

    suspend fun execute(params: Params): Flow<Either<Failure, Return>>
}
