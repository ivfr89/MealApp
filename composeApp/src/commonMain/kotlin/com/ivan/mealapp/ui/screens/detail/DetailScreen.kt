package com.ivan.mealapp.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.ui.common.LoadingIndicator
import com.ivan.mealapp.ui.screens.Screen
import mealapp.composeapp.generated.resources.Res
import mealapp.composeapp.generated.resources.back
import mealapp.composeapp.generated.resources.favorite
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun DetailScreen(id: String, viewModel: DetailViewModel = koinViewModel(), onBack: () -> Unit) {

    LaunchedEffect(Unit) {
        viewModel.handleAction(DetailScreenActions.Initialize(id))
    }

    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Screen {
        Scaffold(
            topBar = {
                DetailTopBar(
                    title = state.meal?.name.orEmpty(),
                    scrollBehavior = scrollBehavior,
                    onBack = onBack
                )
            },
            floatingActionButton = {
                state.meal?.let { movie ->
                    FloatingActionButton(onClick = { viewModel.handleAction(DetailScreenActions.ToggleFavourite) }) {
                        Icon(
                            imageVector = if (movie.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(Res.string.favorite)
                        )
                    }
                }
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { padding ->

            LoadingIndicator(
                enabled = state.loading,
                modifier = Modifier.fillMaxSize().padding(padding)
            )

            state.meal?.let {
                MovieDetail(
                    meal = it,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailTopBar(
    title: String,
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(Res.string.back)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun MovieDetail(
    meal: Meal,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = meal.image,
            contentDescription = meal.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )
        Text(text = meal.category.orEmpty(), modifier = Modifier.padding(16.dp))
        Text(text = meal.tags.toString(), modifier = Modifier.padding(16.dp))
        Text(text = meal.preparationSteps.orEmpty(), modifier = Modifier.padding(16.dp))
    }

}
