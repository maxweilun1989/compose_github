package com.andro.github.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andro.github.ui.screens.RepoListErrorScreen
import com.andro.github.ui.screens.RepoListLoadingScreen
import com.andro.github.ui.screens.RepositoryDetailScreen
import com.andro.github.ui.screens.RepositoryListScreen
import com.andro.github.ui.theme.GithubTheme
import com.andro.github.ui.viewmodel.GitHubViewModel
import com.andro.github.ui.viewmodel.RepositoryListUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: GitHubViewModel by viewModels()
    val language = "kotlin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubTheme {
                Surface(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .systemBarsPadding(),
                ) {
                    val uiState by viewModel.uiState.collectAsState()
                    val navController = rememberNavController()

                    LaunchedEffect(Unit) {
                        viewModel.fetchRepositories(language)
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "loading",
                    ) {
                        composable("loading") {
                            RepoListLoadingScreen()
                        }

                        composable("error") {
                            val error = uiState as? RepositoryListUiState.Error
                            if (error != null) {
                                RepoListErrorScreen(
                                    message = error.message,
                                    onRetry = { viewModel.fetchRepositories(language) },
                                )
                            }
                        }

                        composable("repository_list") {
                            val success = uiState as? RepositoryListUiState.Success
                            if (success != null) {
                                RepositoryListScreen(
                                    repositories = success.data,
                                    onRepositoryClick = { repository ->
                                        navController.navigate("repository_detail/${repository.id}")
                                    },
                                )
                            }
                        }

                        composable(
                            route = "repository_detail/{repositoryId}",
                        ) { backStackEntry ->
                            val repositoryId = backStackEntry.arguments?.getString("repositoryId")
                            val repos = (uiState as RepositoryListUiState.Success).data
                            val repository = repos.find { it.id.toString() == repositoryId }
                            if (repository != null) {
                                RepositoryDetailScreen(repository = repository)
                            }
                        }
                    }

                    val destination =
                        when (uiState) {
                            is RepositoryListUiState.Error -> "error"
                            RepositoryListUiState.Loading -> "loading"
                            is RepositoryListUiState.Success -> "repository_list"
                        }
                    navController.navigate(destination) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}
