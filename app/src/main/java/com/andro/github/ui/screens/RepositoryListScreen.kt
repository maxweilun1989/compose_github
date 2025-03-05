package com.andro.github.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andro.github.data.Repository
import com.andro.github.ui.component.RepositoryCard

@Suppress("ktlint:standard:function-naming")
@Composable
fun RepositoryListScreen(
    repositories: List<Repository>,
    onRepositoryClick: (Repository) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(repositories) { repo ->
            RepositoryCard(repo, onClick = onRepositoryClick)
        }
    }
}
