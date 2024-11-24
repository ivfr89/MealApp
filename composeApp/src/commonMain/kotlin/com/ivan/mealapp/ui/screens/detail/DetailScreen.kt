package com.ivan.mealapp.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.ivan.mealapp.ui.common.LocalizedStrings
import com.ivan.mealapp.ui.screens.Screen
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
                            contentDescription = LocalizedStrings.getString("favorite")
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
                MealDetail(
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
                    contentDescription = LocalizedStrings.getString("back")
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun MealDetail(
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
        SectionTags(title = LocalizedStrings.getString("tags"), tags = meal.tags.orEmpty())
        SectionText(title = LocalizedStrings.getString("category"), content = meal.category.orEmpty())
        SectionText(title = LocalizedStrings.getString("howto"), content = meal.preparationSteps.orEmpty())
    }
}

@Composable
fun SectionText(modifier: Modifier = Modifier, title: String, content: String) {
    if (content.isEmpty()) return

    Column(
        modifier = modifier.padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Text(text = content, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SectionTags(modifier: Modifier = Modifier, title: String, tags: List<String>) {
    if (tags.isEmpty()) return

    Column(
        modifier = modifier.padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tags.forEach {
                AssistChip(
                    onClick = {},
                    label = { Text(it) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    }
}