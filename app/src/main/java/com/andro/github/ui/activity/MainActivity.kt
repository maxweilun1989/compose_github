package com.andro.github.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andro.github.ui.component.screens.RepositoryDetailScreen
import com.andro.github.ui.component.screens.RepositoryListScreen
import com.andro.github.ui.theme.GithubTheme
import com.andro.github.ui.viewmodel.GitHubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: GitHubViewModel by viewModels()

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
                    val repositories by viewModel.repositories.collectAsState()
                    val navController = rememberNavController()
                    viewModel.fetchPopularRepositories()

                    NavHost(
                        navController = navController,
                        startDestination = "repository_list",
                    ) {
                        composable("repository_list") {
                            RepositoryListScreen(
                                repositories = repositories,
                                onRepositoryClick = { repository ->
                                    navController.navigate("repository_detail/${repository.id}")
                                },
                            )
                        }

                        composable(
                            route = "repository_detail/{repositoryId}",
                        ) { backStackEntry ->
                            val repositoryId = backStackEntry.arguments?.getString("repositoryId")
                            val repository = repositories.find { it.id.toString() == repositoryId }
                            if (repository != null) {
                                RepositoryDetailScreen(repository = repository)
                            }
                        }
                    }
                }
            }
        }
    }
}
