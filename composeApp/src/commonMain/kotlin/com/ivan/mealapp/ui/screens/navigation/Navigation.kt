package com.ivan.mealapp.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ivan.mealapp.ui.screens.detail.DetailScreen
import com.ivan.mealapp.ui.screens.home.HomeScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavCommand.ContentType(Feature.MEAL).route
    ) {
        mealGraph(navController)
    }
}

@OptIn(KoinExperimentalAPI::class)
private fun NavGraphBuilder.mealGraph(navController: NavController) {
        composable(NavCommand.ContentType(Feature.MEAL).route) {
            HomeScreen(
                onMovieClick = { meal ->
                    navController.navigate(
                        NavCommand.ContentTypeDetail(
                            Feature.MEAL
                        ).createRoute(meal.id)
                    )
                }
            )
        }

        composable(
            NavCommand.ContentTypeDetail(Feature.MEAL).route,
            arguments = NavCommand.ContentTypeDetail(Feature.MEAL).args
        ) {
            it.arguments?.getString(NavArg.ItemId.key)?.let { id ->
                DetailScreen(
                    id = id,
                    viewModel = koinViewModel(),
                    onBack = { navController.popBackStack() })
            }
        }
}
