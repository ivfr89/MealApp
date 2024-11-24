package com.ivan.mealapp.data.local.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MealDb(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val image: String,
    val favourite: Boolean,
    val preparationSteps: String? = null,
    val category: String? = null,
    val tags: String? = null
)
