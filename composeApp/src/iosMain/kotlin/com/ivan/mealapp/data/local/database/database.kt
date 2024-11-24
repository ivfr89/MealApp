package com.ivan.mealapp.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<MealsDatabase> {
    val dbFilePath = NSHomeDirectory() + "/$DATABASE_NAME"
    return Room.databaseBuilder<MealsDatabase>(
        name = dbFilePath,
        factory =  { MealsDatabase::class.instantiateImpl() }
    )
}