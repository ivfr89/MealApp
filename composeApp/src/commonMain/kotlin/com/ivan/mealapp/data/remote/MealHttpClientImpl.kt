package com.ivan.mealapp.data.remote

import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import okio.IOException

class MealHttpClientImpl(private val client: HttpClient): MealHttpClient {

    override suspend fun <T> safeRequest(
        method: HttpMethod,
        endpoint: String,
        block: HttpRequestBuilder.() -> Unit,
        transform: suspend (HttpResponse) -> T
    ): Either<Failure, T> {
        return try {
            val response: HttpResponse = client.request(endpoint) {
                this.method = method
                block()
            }

            if (response.status.isSuccess()) {
                Either.Right(transform(response))
            } else {
                Either.Left(ServerError(response.status.description))
            }
        } catch (e: IOException) {
            Either.Left(NetworkError)
        } catch (e: Exception) {
            Either.Left(Failure.UnknownError)
        }
    }

}

data class ServerError(val reason: String) : Failure.CustomFailure()
object NetworkError : Failure.CustomFailure()
