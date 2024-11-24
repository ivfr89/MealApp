package com.ivan.mealapp.data.remote

import com.ivan.mealapp.data.remote.models.MealDetailInfoEntity
import com.ivan.mealapp.data.remote.models.MealDetailInfoResultEntity
import com.ivan.mealapp.data.remote.models.MealResultEntity
import com.ivan.mealapp.domain.models.functional.Either
import com.ivan.mealapp.domain.models.functional.Failure
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod

class MealRemoteDataSourceImpl(
    private val client: MealHttpClient,
    private val apiKey: String
) : MealRemoteDataSource {

    override suspend fun getMealsByCategory(category: String): Either<Failure, MealResultEntity> =
        client.safeRequest(
            method = HttpMethod.Get,
            endpoint = "/v1/$apiKey/filter.php",
            block = { parameter(PARAM_CATEGORY, category) },
            transform = { body<MealResultEntity>() }
        )

    override suspend fun getMealById(id: String): Either<Failure, MealDetailInfoEntity> =
        client.safeRequest(
            method = HttpMethod.Get,
            endpoint = "/v1/$apiKey/lookup.php",
            block = { parameter(PARAM_IDENTIFICATION, id) },
            transform = { body<MealDetailInfoResultEntity>().meals.first() }
        )
}

private const val PARAM_CATEGORY = "c"
private const val PARAM_IDENTIFICATION = "i"