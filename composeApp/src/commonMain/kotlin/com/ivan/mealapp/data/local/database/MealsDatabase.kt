package com.ivan.mealapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ivan.mealapp.data.local.database.models.MealDb


interface DB {
    fun clearAllTables() {}
}

@Database(entities = [MealDb::class], version = 1, exportSchema = false)
abstract class MealsDatabase : RoomDatabase(), DB {
    abstract fun mealsDao(): MealsDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<MealsDatabase>
): MealsDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .fallbackToDestructiveMigrationFrom(true,1)
        .build()
}

const val DATABASE_NAME = "meals.db"
