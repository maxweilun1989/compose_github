package com.andro.github.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andro.github.app.AppConfig
import com.andro.github.data.Repository
import com.andro.github.ui.screens.RepoListErrorScreen
import com.andro.github.ui.screens.RepoListLoadingScreen
import com.andro.github.ui.screens.RepositoryDetailScreen
import com.andro.github.ui.screens.RepositoryListScreen
import com.andro.github.ui.viewmodel.GitHubViewModel
import com.andro.github.ui.viewmodel.RepositoryListUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun RepositoryApp(viewModel: GitHubViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val language by viewModel.language.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val drawerContent: @Composable () -> Unit = {
        DrawerContent(navController) { scope.launch { drawerState.close() } }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchRepositories()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = drawerContent,
        scrimColor = Color.Transparent,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(AppConfig.TITLE) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                )
            },
        ) { padding ->
            AppNavigation(navController, uiState, viewModel, language, padding)
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun AppNavigation(
    navController: NavHostController,
    uiState: RepositoryListUiState<List<Repository>>,
    viewModel: GitHubViewModel,
    language: String,
    padding: PaddingValues,
) {
    NavHost(
        modifier = Modifier.padding(padding),
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
                    onRetry = { viewModel.fetchRepositories() },
                )
            }
        }

        composable("repository_list") {
            val success = uiState as? RepositoryListUiState.Success
            if (success != null) {
                RepositoryListScreen(
                    repositories = success.data,
                    language = language,
                    listState = viewModel.llState,
                    onLanguageChange = { newLanguage ->
                        viewModel.setLanguage(newLanguage)
                    },
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

@Suppress("ktlint:standard:function-naming")
@Composable
fun DrawerContent(
    navController: NavController,
    closeDrawer: () -> Unit = {},
) {
    val backgroundColor = MaterialTheme.colorScheme.primary

    Box(
        modifier =
            Modifier
                .fillMaxHeight()
                .width(280.dp)
                .background(backgroundColor),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            navController.navigate("login")
                            closeDrawer
                        },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "点击头像登录",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(32.dp))
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                thickness = 1.dp,
            )

            Spacer(modifier = Modifier.height(16.dp))
            DrawerOption(
                icon = Icons.Default.Settings,
                label = "设置",
                onClick = { /* TODO: 跳转到设置页面 */ },
            )

            DrawerOption(
                icon = Icons.Default.Info,
                label = "关于",
                onClick = { /* TODO: 跳转到关于页面 */ },
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun DrawerOption(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
