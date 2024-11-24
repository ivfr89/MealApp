package com.ivan.mealapp.data.local.database.utils

import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import com.ivan.mealapp.domain.models.functional.toLeft
import com.ivan.mealapp.domain.models.functional.toRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal fun <T, R> Flow<List<T>>.mapList(transform: (T) -> R): Flow<Either<Failure, List<R>>> =
    map { list ->
        if (list.isEmpty())
            Failure.ElementNotFound(EMPTY_LIST_MESSAGE).toLeft()
        else
            list.map(transform).toRight()
    }.distinctUntilChanged()

private const val EMPTY_LIST_MESSAGE = "Empty list"
