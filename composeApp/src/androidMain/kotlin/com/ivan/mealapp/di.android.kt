package com.ivan.mealapp

import com.ivan.mealapp.data.local.database.getDatabaseBuilder
import org.koin.dsl.module

actual val nativeModule = module {
    single { getDatabaseBuilder(get()) }
}