package com.ivan.mealapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MealApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MealApp)
        }
    }
}