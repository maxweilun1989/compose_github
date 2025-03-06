package com.andro.github.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andro.github.app.AppConfig
import com.andro.github.data.Repository
import com.andro.github.ui.component.RepositoryCard

@Suppress("ktlint:standard:function-naming")
@Composable
fun RepositoryListScreen(
    repositories: List<Repository>,
    language: String,
    listState: LazyListState,
    sortDescending: Boolean,
    onLanguageChange: (String) -> Unit,
    onRepositoryClick: (Repository) -> Unit,
    onSortClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LanguageDropdown(
                selectedLanguage = language,
                onLanguageChange = onLanguageChange,
            )

            TextButton(onClick = onSortClick) {
                Text(text = if (sortDescending) "Sort by Stars ↓" else "Sort by Stars ↑")
            }
        }
        LazyColumn(
            state = listState,
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
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun LanguageDropdown(
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .width(200.dp)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp),
                ).clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = selectedLanguage,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        )

        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown Arrow",
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface,
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            AppConfig.LNAGUAGE_LIST.forEach { language ->
                DropdownMenuItem(
                    onClick = {
                        onLanguageChange(language)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = language,
                            style =
                                MaterialTheme.typography.bodyLarge.copy(
                                    color =
                                        if (language == selectedLanguage) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        },
                                ),
                        )
                    },
                )
            }
        }
    }
}
