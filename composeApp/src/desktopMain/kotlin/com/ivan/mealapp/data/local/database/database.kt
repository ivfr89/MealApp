package com.ivan.mealapp.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<MealsDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "mealapp.db")
    return Room.databaseBuilder<MealsDatabase>(
        name = dbFile.absolutePath,
    )
}