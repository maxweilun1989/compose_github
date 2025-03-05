package com.andro.github.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.andro.github.network.Repository
import com.andro.github.ui.GitHubViewModel
import com.andro.github.ui.theme.GithubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: GitHubViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubTheme {
                setContent {
                    val repositories by viewModel.repositories.collectAsState()

                    viewModel.fetchPopularRepositories()

                    RepositoryList(repositories)
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun RepositoryList(repositories: List<Repository>) {
    LazyColumn {
        items(repositories) { repo ->
            Text(text = "${repo.name} - 🌟 ${repo.stars}")
        }
    }
}
