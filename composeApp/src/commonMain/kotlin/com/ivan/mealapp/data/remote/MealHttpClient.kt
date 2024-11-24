package com.ivan.mealapp.data.remote

import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

interface MealHttpClient {

    suspend fun <T> safeRequest(
        method: HttpMethod,
        endpoint: String,
        block: HttpRequestBuilder.() -> Unit = {},
        transform: suspend HttpResponse.() -> T
    ): Either<Failure, T>
}
