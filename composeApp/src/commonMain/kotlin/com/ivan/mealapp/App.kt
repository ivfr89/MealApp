package com.ivan.mealapp

import androidx.compose.runtime.Composable
import com.ivan.mealapp.ui.screens.navigation.Navigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        Navigation()
    }
}