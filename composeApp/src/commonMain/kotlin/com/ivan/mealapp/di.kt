package com.ivan.mealapp

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ivan.mealapp.data.MealRepositoryImpl
import com.ivan.mealapp.data.local.MealLocalDataSource
import com.ivan.mealapp.data.local.MealLocalDataSourceImpl
import com.ivan.mealapp.data.remote.MealRemoteDataSourceImpl
import com.ivan.mealapp.data.local.database.MealsDao
import com.ivan.mealapp.data.local.database.MealsDatabase
import com.ivan.mealapp.data.remote.MealHttpClient
import com.ivan.mealapp.data.remote.MealHttpClientImpl
import com.ivan.mealapp.data.remote.MealRemoteDataSource
import com.ivan.mealapp.domain.repository.MealRepository
import com.ivan.mealapp.domain.usecases.GetMealById
import com.ivan.mealapp.domain.usecases.GetMealsByCategory
import com.ivan.mealapp.domain.usecases.ToggleFavouriteMeal
import com.ivan.mealapp.ui.screens.detail.DetailViewModel
import com.ivan.mealapp.ui.screens.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single(named(QUALIFIER_API_KEY_NAME)) { BuildConfig.API_KEY }
    single<MealsDao> {
        val dbBuilder = get<RoomDatabase.Builder<MealsDatabase>>()
        dbBuilder.setDriver(BundledSQLiteDriver()).build().mealsDao()
    }
}

val dataModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.SIMPLE
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BASE_URL
                }
            }
        }
    }

    factoryOf(::MealHttpClientImpl) bind MealHttpClient::class
    factoryOf(::MealRepositoryImpl) bind MealRepository::class
    factoryOf(::MealLocalDataSourceImpl) bind MealLocalDataSource::class
    factory<MealRemoteDataSource> {
        MealRemoteDataSourceImpl(
            client = get(),
            apiKey = get(named(QUALIFIER_API_KEY_NAME))
        )
    }
}

val domainModule = module {
    factoryOf(::ToggleFavouriteMeal)
    factoryOf(::GetMealById)
    factoryOf(::GetMealsByCategory)
}

val viewModelsModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailViewModel)
}

expect val nativeModule: Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, domainModule, dataModule, viewModelsModule, nativeModule)
    }
}

private const val QUALIFIER_API_KEY_NAME = "apiKey"
private const val BASE_URL = "www.themealdb.com/api/json/"