package com.ivan.mealapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ivan.mealapp.domain.models.Meal
import com.ivan.mealapp.ui.common.CircularIconWithText
import com.ivan.mealapp.ui.common.LoadingIndicator
import com.ivan.mealapp.ui.common.LocalizedStrings
import com.ivan.mealapp.ui.screens.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    onMovieClick: (Meal) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.handleAction(HomeScreenActions.Initialize(CategoryType.SEAFOOD))
    }

    Screen {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = LocalizedStrings.getString("app_name")) },
                    scrollBehavior = scrollBehavior
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { padding ->
            val state by viewModel.state.collectAsState()

            LoadingIndicator(
                enabled = state.loading,
                modifier = Modifier.fillMaxSize().padding(padding)
            )

            Column(
                modifier = Modifier.padding(padding)
            ) {
                CategoryLayout {
                    viewModel.handleAction(it)
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(120.dp),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.meals, key = { it.id }) {
                        MealItem(meal = it) { onMovieClick(it) }
                    }
                }
            }
        }
    }
}

@Composable
fun MealItem(meal: Meal, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box {
            AsyncImage(
                model = meal.image,
                contentDescription = meal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2 / 3f)
                    .clip(MaterialTheme.shapes.small)
            )
            if (meal.isFavourite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = LocalizedStrings.getString("favorite"),
                    tint = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
        Text(
            text = meal.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun CategoryLayout(
    modifier: Modifier = Modifier,
    onAction: (HomeScreenActions) -> Unit = {}
) {

    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CircularIconWithText(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = LocalizedStrings.getString("seafood"),
            text = LocalizedStrings.getString("seafood"),
            onClick = { onAction(HomeScreenActions.Initialize(CategoryType.SEAFOOD)) }
        )

        CircularIconWithText(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = LocalizedStrings.getString("chicken"),
            text = LocalizedStrings.getString("chicken"),
            onClick = { onAction(HomeScreenActions.Initialize(CategoryType.CHICKEN)) }
        )

        CircularIconWithText(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = LocalizedStrings.getString("starter"),
            text = LocalizedStrings.getString("starter"),
            onClick = { onAction(HomeScreenActions.Initialize(CategoryType.STARTER)) }
        )

        CircularIconWithText(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = LocalizedStrings.getString("breakfast"),
            text = LocalizedStrings.getString("breakfast"),
            onClick = { onAction(HomeScreenActions.Initialize(CategoryType.BREAKFAST)) }
        )
    }
}