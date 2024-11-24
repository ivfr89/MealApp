package com.ivan.mealapp.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface CoroutineUtils {

    val coroutineContext: CoroutineContext

    suspend fun <T> T.postOn(stateFlow: MutableStateFlow<T>) {
        withContext(coroutineContext) {
            stateFlow.value = this@postOn
        }
    }

    suspend fun <T> T.postOn(sharedFlow: MutableSharedFlow<T>) {
        withContext(coroutineContext) {
            sharedFlow.emit(this@postOn)
        }
    }

    fun <T> CoroutineScope.launchAndPost(
        stateFlow: MutableStateFlow<T>,
        context: CoroutineContext = coroutineContext,
        block: suspend CoroutineScope.() -> T
    ) {
        launch(context = context) {
            block().postOn(stateFlow)
        }
    }

    fun <T> CoroutineScope.launchAndPost(
        sharedFlow: MutableSharedFlow<T>,
        context: CoroutineContext = coroutineContext,
        block: suspend CoroutineScope.() -> T
    ) {
        launch(context = context) {
            block().postOn(sharedFlow)
        }
    }
}
