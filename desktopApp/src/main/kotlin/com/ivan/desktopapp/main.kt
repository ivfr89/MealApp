package com.ivan.desktopapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ivan.mealapp.App
import com.ivan.mealapp.initKoin
import com.ivan.mealapp.ui.common.LocalizedStrings

fun main() = application {
    initKoin()

    Window(onCloseRequest = ::exitApplication, title = LocalizedStrings.getString("app_name")) {
        App()
    }
}